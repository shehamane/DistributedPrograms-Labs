package bmstu.bigdata.lab4;

public class GetTestResultCommand {
    private final int packageId;

    public GetTestResultCommand(int packageId, TestResult testResult) {
        this.packageId = packageId;
    }

    public int getPackageId() {
        return packageId;
    }
}
