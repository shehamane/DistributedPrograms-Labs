package bmstu.bigdata.lab4;

public class Test{
    private final String name;
    private final String expectedResult;
    private final Object[] parameters;


    public Test(String name, String expectedResult, Object[] parameters){
        this.name = name;
        this.expectedResult = expectedResult;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public String getExpectedResult() {
        return expectedResult;
    }
}