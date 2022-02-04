package bmstu.bigdata.lab4;

public class StoreCommand {
    private final TestResult testResult;
    private final int packageId;


    public StoreCommand(TestResult testResult, int packageId) {
        this.testResult = testResult;
        this.packageId = packageId;
    }

    public TestResult getTestResult() {
        return testResult;
    }

    public int getPackageId() {
        return packageId;
    }
}
