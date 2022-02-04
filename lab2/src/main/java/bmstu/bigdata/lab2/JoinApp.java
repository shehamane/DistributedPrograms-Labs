package bmstu.bigdata.lab2;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.Job;

public class JoinApp {
    public static void main(String args[]) throws Exception{
        if (args.length != 3){
            System.err.println("args err");
            System.exit(-1);
        }

        Job job = Job.getInstance();
        job.setJarByClass(JoinApp.class);
        job.setJobName("JoinApp");
        MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, RaceMapper.class);
        MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, AirportMapper.class);
        FileOutputFormat.setOutputPath(job, new Path(args[2]));
        job.setGroupingComparatorClass(AirportKeyComparable.AirportCodeGroupingComparator.class);
        job.setPartitionerClass(AirportKeyComparable.AirportCodePartitioner.class);
        job.setReducerClass(AirportDelayReducer.class);
        job.setMapOutputKeyClass(AirportKeyComparable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setNumReduceTasks(1);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
