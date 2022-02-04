package bmstu.bigdata.lab4;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.routing.RoundRobinPool;

public class Router extends AbstractActor {
    private final ActorRef executorsPool, resultKeeper;

    public Router() {
        resultKeeper = getContext().actorOf(Props.create(ResultKeeper.class));
        executorsPool = getContext().actorOf(
                new RoundRobinPool(5).props(
                        Props.create(Executor.class, resultKeeper)
                )
        );
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(
                        GetTestResultCommand.class, this::onGetTestResultCommand
                ).match(
                        ExecuteTestsCommand.class, this::onExecuteTestsCommand
                ).build();
    }

    private void onGetTestResultCommand(GetTestResultCommand command) {
        resultKeeper.forward(command, getContext());
    }

    private void onExecuteTestsCommand(ExecuteTestsCommand command) {
        for (Test test : command.getTest()) {
            ExecuteTestCommand subCommand = new ExecuteTestCommand(
                    command.getPackageId(),
                    command.getJsScript(),
                    command.getFunctionName(),
                    test
            );
            executorsPool.tell(subCommand, ActorRef.noSender());
        }
    }
}
