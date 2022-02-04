package bmstu.bigdata.lab6;

public class GetServerCommand {
    private final String url;

    public GetServerCommand(String url){
        this.url = url;
    }

    public String getCurrentUrl(){
        return url;
    }
}
