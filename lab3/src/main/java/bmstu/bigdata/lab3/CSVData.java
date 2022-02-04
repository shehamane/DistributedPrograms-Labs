package bmstu.bigdata.lab3;

public class CSVData {
    private String[] cols;

    public CSVData(String[] cols) {
        this.cols = cols;
    }

    public String getValue(int colNumber){
        return cols[colNumber];
    }

    public int getInt(int colNumber){
        return Integer.parseInt(getValue(colNumber));
    }
}
