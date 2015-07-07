import org.apache.spark.streaming.Time;
import scala.*;
import scala.Double;

import java.io.*;
import java.lang.Float;

/**
 * Created by Pedro on 07/07/2015.
 */
public class CountWriter {
    private File outdir;
    private PrintWriter count_file;

    CountWriter(String outdir) throws IOException {
        this.outdir = new File(outdir);
        count_file = new PrintWriter(new BufferedWriter(new FileWriter(new File(this.outdir,"categories_count.txt"), true)));
    }
    public void appendCategoryCount(Tuple2<Tuple2<String, String>, scala.Double>[] rdds,Time time){
        for( Tuple2<Tuple2<String, String>, scala.Double> rdd:  rdds) {
            double count =scala.Double.unbox(rdd._2());
            count_file.format("%s %d %d \n", rdd._1()._2(), (int) count, time.milliseconds() / 1000L);
            count_file.flush();
            //count_file.format("%s %d \n", rdd._1()._2(), time.milliseconds() / 1000L);
        }
    }
}
