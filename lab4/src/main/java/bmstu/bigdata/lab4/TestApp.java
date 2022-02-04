package bmstu.bigdata.lab4;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class TestApp {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("javascript_code_test");
        ActorRef router = system.actorOf(Props.create(Router.class));
    }
}
