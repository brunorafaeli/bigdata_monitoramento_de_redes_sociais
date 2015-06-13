/**
 * Created by berne on 26/05/15.
 */
/* HelloStreamApp.scala */

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.streaming._

object HelloStreamApp {
  def main(args: Array[String]) {
    val conf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("Hello Stream Application")
    val ssc = new StreamingContext(conf, Seconds(5))

    val lines = ssc.socketTextStream("localhost", 9999)
    val words = lines.flatMap(_.split(" "))

    val pairs = words.map(word => (word, 1))
    val wordCounts = pairs.reduceByKey(_ + _)


    // Print the first ten elements of each RDD generated in this DStream to the console
    wordCounts.print()

    ssc.start()
    ssc.awaitTermination()
  }
}