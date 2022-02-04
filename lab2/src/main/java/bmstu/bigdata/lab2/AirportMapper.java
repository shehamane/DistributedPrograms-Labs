package bmstu.bigdata.lab2;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class AirportMapper extends Mapper<LongWritable, Text, AirportKeyComparable, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] values = value.toString().split(",");
        int id = Integer.parseInt(values[0].replace("\"", ""));
        String name = values[1].replace("\"", "");
        context.write(new AirportKeyComparable(id, false), new Text(name));
    }
}
