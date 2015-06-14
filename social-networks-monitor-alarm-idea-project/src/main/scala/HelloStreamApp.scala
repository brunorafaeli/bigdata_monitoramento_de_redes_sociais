/**
 * Created by berne on 26/05/15.
 */
/* HelloStreamApp.scala */

import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import scala.collection.JavaConversions._

object HelloStreamApp {
  def main(args: Array[String]) {

    val Array(consumerKey, consumerSecret, accessToken, accessTokenSecret) = args.take(4)
    //val filters = args.takeRight(args.length - 4)

    // Set the system properties so that Twitter4j library used by twitter stream
    // can use them to generat OAuth credentials
    System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)
    val dictCompany = new NamedDictionary("C:\\devel\\bigdata\\bigdata_monitoramento_de_redes_sociais\\social-networks-monitor-alarm-idea-project\\src\\main\\resources\\dict_ibm.json")
    val filters = dictCompany.getTermsArray
    val conf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("Hello Stream Application")
    val ssc = new StreamingContext(conf, Seconds(15))
    //val lines = ssc.socketTextStream("localhost", 9999)
    val twstream = TwitterUtils.createStream(ssc, None, filters)
    val twcollector = twstream.map(status => (dictCompany.getName,status.getText))

    //unifiedStream will aggregate all input streams in the future
   // val unifiedStream = tw_txt.union()
    val unifiedStream = twcollector
    val words = unifiedStream.flatMapValues(_.toLowerCase.split(" "))

    val pairs = words.map({case (company,word) => ((company,word), 1.0)})
    val wordCounts = pairs.reduceByKey(_+_)
    val wordCountswindow = wordCounts.window(Seconds(120), Seconds(15)).groupByKey
    val x = wordCountswindow.mapValues( value => org.apache.spark.util.StatCounter(value))


    // Print the first ten elements of each RDD generated in this DStream to the console
    wordCounts.print()
    unifiedStream.print()
    x.print()

    ssc.start()
    ssc.awaitTermination()
  }
}