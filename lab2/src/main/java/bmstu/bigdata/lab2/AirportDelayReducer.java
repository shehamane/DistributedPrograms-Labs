package bmstu.bigdata.lab2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class AirportDelayReducer extends Reducer<AirportKeyComparable, Text, Text, Text> {
    @Override
    protected void reduce(AirportKeyComparable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Iterator<Text> it = values.iterator();

        double max = 0, min = 999999, sum = 0;
        int raceCount = 0;
        String name = it.next().toString();
        while (it.hasNext()){
            Text delayText = it.next();
            double delay = Double.parseDouble(delayText.toString());

            ++raceCount;
            if (delay < min){
                min = delay;
            }
            if (delay > max){
                max = delay;
            }
            sum+= delay;
        }
        if (raceCount>0){
            String result = min + " " + max + " " + sum/raceCount;
            context.write(new Text (name), new Text(result));
        }
    }
}
