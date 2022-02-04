package bmstu.bigdata.lab6;


import akka.actor.ActorRef;
import akka.event.LoggingAdapter;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

import static akka.http.javadsl.server.Directives.*;

public class Router {
    private final Http http;
    private final ActorRef storage;
    private final String currentServerUrl;
    private final LoggingAdapter log;

    public Router(Http http, ActorRef storage, String currentServerUrl, LoggingAdapter log) {
        this.http = http;
        this.storage = storage;
        this.currentServerUrl = currentServerUrl;
        this.log = log;
    }

    public Route create() {
        return parameter(
                "url",
                url -> parameter(
                        "count",
                        countParam -> get(() -> {
                                    log.info(String.format("Request: url: %s count: %s", url, countParam));
                                    int count = Integer.parseInt(countParam);
                                    if (count == 0) {
                                        return completeWithFuture(http.singleRequest(HttpRequest.create(url)));
                                    } else {
                                        CompletionStage<HttpResponse> cs = Patterns.ask(
                                                        storage,
                                                        new GetServerCommand(currentServerUrl),
                                                        Duration.ofSeconds(1))
                                                .thenCompose(curl -> {
                                                    try {
                                                        return http.singleRequest(HttpRequest.create(String.format(
                                                                "http://%s/?url=%s&count=%d",
                                                                curl,
                                                                URLEncoder.encode(url, StandardCharsets.UTF_8.toString()),
                                                                count - 1
                                                        )));
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                        return null;
                                                    }
                                                });
                                        return completeWithFuture(cs);
                                    }
                                }
                        )));
    }
}
