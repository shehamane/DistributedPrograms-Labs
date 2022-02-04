package bmstu.bigdata.lab1;

import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class LetterMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        for (char c : line.toCharArray()) {
            if (Character.isLetter(c)) {
                context.write(new Text(Character.toString(Character.toLowerCase(c))), new IntWritable(1));
            }
        }
    }
}
