package bmstu.bigdata.lab3;

import scala.Tuple2;

public class AirportHandler {
    public static Tuple2<Integer, String> readStringCSV(String s) {
        CSVData data = new CSVData(CSVParser.parseValues(s, ","));
        int id = data.getInt(0);
        String name = data.getValue(1);
        return new Tuple2<>(id, name);
    }
}
