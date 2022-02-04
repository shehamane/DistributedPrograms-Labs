package bmstu.bigdata.lab3;

public class CSVParser {
    public static String removeQuotes(String s){
        return s.replace("\"", "").replace("\'", "");
    }

    public static String[] parseValues(String s, String delimiter){
        String[] cols = s.split(delimiter);
        for (int i = 0; i<cols.length; ++i)
            cols[i] = removeQuotes(cols[i]);
        return cols;
    }
}
