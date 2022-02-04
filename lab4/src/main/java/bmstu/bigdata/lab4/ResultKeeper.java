package bmstu.bigdata.lab4;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultKeeper extends AbstractActor {
    private final HashMap<Integer, ArrayList<TestResult>> storage = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(
                StoreCommand.class, this::onStoreCommand
        ).match(
                GetTestResultCommand.class, this::onGetTestResultCommand
                ).build();
    }

    private void onStoreCommand(StoreCommand command) {
        this.storage.computeIfAbsent(
                command.getPackageId(),
                k -> new ArrayList<>()
        ).add(command.getTestResult());
    }

    private void onGetTestResultCommand(GetTestResultCommand command) {
        ArrayList<TestResult> requestedResults =
                storage.get(command.getPackageId());
        sender().tell(requestedResults, ActorRef.noSender());
    }
}
