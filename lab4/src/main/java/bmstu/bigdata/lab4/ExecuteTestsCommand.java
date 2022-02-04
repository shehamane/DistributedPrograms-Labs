package bmstu.bigdata.lab4;

import java.util.ArrayList;

public class ExecuteTestsCommand {
    private final int packageId;
    private final String jsScript;
    private final String functionName;
    private final ArrayList<Test> tests;

    public ExecuteTestsCommand(int packageId, String jsScript,
                               String functionName, ArrayList<Test> tests) {
        this.packageId = packageId;
        this.jsScript = jsScript;
        this.functionName = functionName;
        this.tests = tests;
    }

    public int getPackageId() {
        return packageId;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getJsScript() {
        return jsScript;
    }

    public ArrayList<Test> getTest() {
        return tests;
    }
}