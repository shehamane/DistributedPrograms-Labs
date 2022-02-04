package bmstu.bigdata.lab6;

public class UpdateServersStateCommand {
    private final String[] urls;

    public UpdateServersStateCommand(String[] urls){
        this.urls = urls;
    }

    public String[] getUrls(){
        return urls;
    }
}
