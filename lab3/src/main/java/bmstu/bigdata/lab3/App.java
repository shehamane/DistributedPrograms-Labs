package bmstu.bigdata.lab3;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.Map;

public class App {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("lab3").setMaster("local[2]").set("spark.executor.memory", "1g");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> airportsDS = sc.textFile("L_AIRPORT_ID.csv");
        JavaRDD<String> raceDS = sc.textFile("664600583_T_ONTIME_sample.csv");

        JavaPairRDD<Integer, String> airportsDict = airportsDS.mapToPair(
                AirportHandler::readStringCSV
        );
        Map<Integer, String> airportsMap = airportsDict.collectAsMap();
        final Broadcast<Map<Integer, String>> airportsBroadcast = sc.broadcast(airportsMap);

        JavaPairRDD<Tuple2<Integer, Integer>, RaceInfo> racesInfo = raceDS.mapToPair(
                s -> new Tuple2<>(RaceInfo.getKey(s), new RaceInfo(s))
        );
        JavaPairRDD<Tuple2<Integer, Integer>, RaceInfo> racesInfoAggr = racesInfo.reduceByKey(
                (r1, r2) -> new RaceInfo(r1, r2)
        );
        JavaPairRDD<Tuple2<String, String>, RaceInfo> withNames = racesInfoAggr.mapToPair(
                t -> new Tuple2<>(new Tuple2<>(airportsBroadcast.value().get(t._1._1),
                        airportsBroadcast.value().get(t._1._2)), t._2)
        );


        withNames.map(s -> s._1 + " " + s._2.getDelay() + " " + (double) s._2.getCancelled() / s._2.getNum()).saveAsTextFile("result");
    }
}
