package bmstu.bigdata.lab6;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.List;

public class ServersWatcher implements Watcher {
    private final ActorRef storageActor;
    private ZooKeeper zoo;

    public void setZoo(ZooKeeper zoo) {
        this.zoo = zoo;
    }

    public ServersWatcher(ActorRef storageActor) {
        this.storageActor = storageActor;
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
        List<String> servers;
        try {
            servers = zoo.getChildren("/servers", this);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        ArrayList<String> urls = new ArrayList<>();
        for (String s : servers) {
            byte[] data;
            try {
                data = zoo.getData("/servers/" + s, false, null);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            urls.add(new String(data));
        }
        System.out.println("Servers list updated!");
        storageActor.tell(new UpdateServersStateCommand(urls.toArray(new String[0])), ActorRef.noSender());
    }
}
