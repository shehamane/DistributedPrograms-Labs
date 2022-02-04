package bmstu.bigdata.lab6;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

import java.util.Random;

public class StorageActor extends AbstractActor {
    private String[] urls;

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create().match(
                GetServerCommand.class, command -> {
                    String server = urls[new Random().nextInt(urls.length)];
                    sender().tell(server, ActorRef.noSender());
                }
        ).match(
                UpdateServersStateCommand.class, command -> urls = command.getUrls()
        ).build();
    }
}
