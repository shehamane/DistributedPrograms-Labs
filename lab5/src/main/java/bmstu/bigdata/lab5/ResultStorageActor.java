package bmstu.bigdata.lab5;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.Pair;
import akka.japi.pf.ReceiveBuilder;
import scala.Int;
import scala.Option;

import java.util.HashMap;
import java.util.Optional;

public class ResultStorageActor extends AbstractActor {
    private final HashMap<Pair<String, Integer>, TestResult> storage = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create().match(
                TestResult.class,
                testResult ->
                        storage.put(
                                new Pair<>(
                                        testResult.getUrl(),
                                        testResult.getCount()
                                ),
                                testResult)
        ).match(
                GetTestResultCommand.class,
                getTestResultCommand -> {
                    Pair<String, Integer> key = new Pair<>(
                            getTestResultCommand.getUrl(),
                            getTestResultCommand.getCount()
                    );
                    Optional<TestResult> result = Optional.ofNullable(storage.get(key));
                    sender().tell(result, ActorRef.noSender());
                }
        ).build();
    }
}
