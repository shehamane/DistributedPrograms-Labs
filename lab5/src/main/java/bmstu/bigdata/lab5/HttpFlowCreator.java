package bmstu.bigdata.lab5;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.japi.Pair;
import akka.pattern.Patterns;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import org.asynchttpclient.AsyncHttpClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class HttpFlowCreator {
    private final Materializer materializer;
    private final ActorRef storageActor;


    public HttpFlowCreator(Materializer materializer, ActorRef storageActor) {
        this.materializer = materializer;
        this.storageActor = storageActor;
    }

    Flow<HttpRequest, HttpResponse, NotUsed> create() {
        return Flow.of(HttpRequest.class).map(
                request -> {
                    Query query = request.getUri().query();
                    String url = query.getOrElse("testUrl", "");
                    Integer count = query.get("count").map(Integer::parseInt).orElse(1);
                    return new Pair<>(url, count);
                }).mapAsync(10, request -> {
            GetTestResultCommand command = new GetTestResultCommand(
                    request.first(),
                    request.second()
            );
            return Patterns.ask(storageActor, command, Duration.ofMillis(3000)).
                    thenCompose(response -> {
                        Optional<TestResult> resultOptional = (Optional<TestResult>) response;
                        if (resultOptional.isPresent()) {
                            TestResult result = resultOptional.get();
                            long time = result.getTime() / result.getCount();
                            System.out.format("%d\n", time);
                        }
                        Sink<Pair<String, Integer>, CompletionStage<Long>> sink = createSink();
                        return Source.from(Collections.singletonList(request)).
                                toMat(sink, Keep.right()).run(materializer);
                    });
        }).map(time -> HttpResponse.create().withEntity(time.toString()));
    }

    private Sink<Pair<String, Integer>, CompletionStage<Long>> createSink(){
        return Flow.<Pair<String, Integer>>create().mapConcat(
                param -> {
                    ArrayList<Pair<String, Integer>> list = new ArrayList<>();
                    for (int i = 0; i<param.second(); ++i){
                        list.add(param);
                    }
                    return list;
                }
        ).mapAsync(10, param->{
            AsyncHttpClient client = asyncHttpClient();
            long startTime = System.currentTimeMillis();
            return client.prepareGet(param.first()).execute().toCompletableFuture().thenCompose(
                    response -> {
                        long time = System.currentTimeMillis() - startTime;
                        System.out.format("Request completed in %d\n", time);
                        return CompletableFuture.completedFuture(new TestResult(param.first(), 1, time));
                    }
            );
        }).fold(new TestResult("", 0, 0), TestResult::merge).map(result -> {
            storageActor.tell(result, ActorRef.noSender());
            return result.getTime()/result.getCount();
        }).toMat(Sink.head(), Keep.right());
    }

}
