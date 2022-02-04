package bmstu.bigdata.lab5;

public class GetTestResultCommand {
    private final String url;
    private final int count;


    public GetTestResultCommand(String url, int count) {
        this.url = url;
        this.count = count;
    }

    public String getUrl(){
        return url;
    }

    public int getCount(){
        return count;
    }
}
