package bmstu.bigdata.lab6;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.typesafe.config.Config;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;


public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Enter port number");
            return;
        }
        int port = Integer.parseInt(args[0]);

        ActorSystem system = ActorSystem.create();

        ActorRef storage = system.actorOf(Props.create(StorageActor.class));

        ServersWatcher watcher = new ServersWatcher(storage);
        ZooKeeper zoo;
        try {
            zoo = new ZooKeeper("127.0.0.1:2181", 3000, watcher);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        watcher.setZoo(zoo);

        String currentUrl = String.format("127.0.0.1:%d", port);
        try {
            zoo.create("/servers/s",
                    currentUrl.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE ,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = new Router(http, storage, currentUrl, materializer.logger())
                .create().flow(system, materializer);
        http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", port), materializer);
    }
}
