package bmstu.bigdata.lab4;

public class TestResult {
    private final String obtainedResult;
    private final Test test;

    public TestResult(String obtainedResult, Test test) {
        this.obtainedResult = obtainedResult;
        this.test = test;
    }

    public Test getTest() {
        return test;
    }

    public String getObtainedResult() {
        return obtainedResult;
    }
}
