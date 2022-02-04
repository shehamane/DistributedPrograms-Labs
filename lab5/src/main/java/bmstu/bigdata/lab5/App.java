package bmstu.bigdata.lab5;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

public class App {
    public static void main(String[] args) throws IOException {
        ActorSystem system = ActorSystem.create("stress_testing");
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        ActorRef resultStorage = system.actorOf(Props.create(ResultStorageActor.class));
        HttpFlowCreator flowCreator = new HttpFlowCreator(materializer, resultStorage);
        final Flow<HttpRequest, HttpResponse, NotUsed> flow = flowCreator.create();
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                flow,
                ConnectHttp.toHost("localhost", 8081),
                materializer
        );
        System.out.println("Server started");
        System.in.read();
        binding.thenCompose(ServerBinding::unbind).thenAccept(unbound -> system.terminate());
    }
}
