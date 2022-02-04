package bmstu.bigdata.lab3;

import scala.Tuple2;

import java.io.Serializable;
import java.util.Objects;

public class RaceInfo implements Serializable {
    private static final int SRC_ID_COL = 11;
    private static final int DST_ID_COL = 14;
    private static final int DELAY_COL = 18;
    private static final int CANCELLED_COL = 19;

    private final double delay;
    private final int cancelled;
    private final int num;

    public RaceInfo(double delay, int cancelled) {
        this.delay = delay;
        this.cancelled = cancelled;
        this.num = 1;
    }

    public RaceInfo(String csvString){
        CSVData data = new CSVData(CSVParser.parseValues(csvString, ","));
        String delay_str = data.getValue(DELAY_COL);
        if (Objects.equals(delay_str, "")) {
            delay_str = "0.0";
        }
        this.delay = Double.parseDouble(delay_str);
        this.cancelled = this.delay > 0 ? 1 : (int)Double.parseDouble(data.getValue(CANCELLED_COL));
        this.num = 1;
    }

    public RaceInfo(RaceInfo l, RaceInfo r){
        this.delay = Math.max(l.delay, r.delay);
        this.num = l.num + r.num;
        this.cancelled = l.cancelled + r.cancelled;
    }

    public double getDelay() {
        return  delay;
    }
    public int getCancelled(){
        return cancelled;
    }

    public int getNum(){
        return num;
    }

    public static Tuple2<Integer, Integer> getKey(String csvString){
        CSVData data = new CSVData(CSVParser.parseValues(csvString, ","));
        int src_id = data.getInt(SRC_ID_COL);
        int dest_id = data.getInt(DST_ID_COL);
        return new Tuple2<>(src_id, dest_id);
    }
}
