package bmstu.bigdata.lab5;

public class TestResult {
    private final int count;
    private final String url;
    private final long time;

    public TestResult(String url, int count, long time){
        this.url = url;
        this.count = count;
        this.time = time;
    }

    public String getUrl(){
        return url;
    }

    public int getCount(){
        return count;
    }

    public long getTime(){
        return time;
    }

    public TestResult merge(TestResult result){
        return new TestResult(result.url, count + result.getCount(), time + result.getTime());
    }
}
