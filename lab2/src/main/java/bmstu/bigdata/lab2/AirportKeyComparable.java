package bmstu.bigdata.lab2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Partitioner;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AirportKeyComparable implements WritableComparable<AirportKeyComparable> {
    private int key;
    private boolean flag;

    public AirportKeyComparable() {
        key = 0;
        flag = false;
    }

    public AirportKeyComparable(int k, boolean f) {
        key = k;
        flag = f;
    }

    @Override
    public int compareTo(AirportKeyComparable o) {
        return key == o.key ? Boolean.compare(flag, o.flag) : Integer.compare(key, o.key);
    }

    public int compareByKey(AirportKeyComparable o) {
        return Integer.compare(key, o.key);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(key);
        dataOutput.writeBoolean(flag);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        key = dataInput.readInt();
        flag = dataInput.readBoolean();
    }

    public static class AirportCodePartitioner extends Partitioner<AirportKeyComparable, Text> {
        public int getPartition(AirportKeyComparable key, Text text, int numTasks) {
            return Integer.hashCode(key.key) % numTasks;
        }
    }

    public static class AirportCodeGroupingComparator extends WritableComparator {
        public AirportCodeGroupingComparator() {
        }

        public int compare(byte[] b1, int s1, int l1, byte b2[], int s2, int l2) {
            int a = readInt(b1, s1);
            int b = readInt(b2, s2);
            return Integer.compare(a, b);
        }

        public int compare(WritableComparable a, WritableComparable b) {
            AirportKeyComparable a1 = (AirportKeyComparable) a;
            AirportKeyComparable b1 = (AirportKeyComparable) b;
            return a1.compareByKey(b1);
        }
    }
}
