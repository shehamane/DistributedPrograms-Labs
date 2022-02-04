package bmstu.bigdata.lab4;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Executor extends AbstractActor {
    private final ActorRef resultKeeper;

    public Executor(ActorRef resultKeeper) {
        this.resultKeeper = resultKeeper;
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create().match(
                ExecuteTestCommand.class, this::onExecuteTestCommand
        ).build();
    }

    private void onExecuteTestCommand(ExecuteTestCommand command) throws ScriptException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager()
                .getEngineByName("nashorn");
        engine.eval(command.getJsScript());
        Invocable invocable = (Invocable) engine;
        String invokeResult = invocable.invokeFunction(
                command.getFunctionName(),
                command.getTest().getParameters()
        ).toString();
        TestResult testResult = new TestResult(invokeResult, command.getTest());
        resultKeeper.tell(
                new StoreCommand(
                        testResult, command.getPackageId()
                ),
                ActorRef.noSender()
        );
    }

}
