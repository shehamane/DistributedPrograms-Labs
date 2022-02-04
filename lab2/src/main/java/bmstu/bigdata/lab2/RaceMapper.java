package bmstu.bigdata.lab2;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class RaceMapper extends Mapper<LongWritable, Text, AirportKeyComparable, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] values = value.toString().split(",");
        int id = Integer.parseInt(values[14].replace("\"", ""));
        String delay = values[18].replace("\"", "");
        if (!delay.isEmpty() &&  Double.parseDouble(delay) > 0) {
            context.write(new AirportKeyComparable(id, true), new Text(delay));
        }
    }
}
