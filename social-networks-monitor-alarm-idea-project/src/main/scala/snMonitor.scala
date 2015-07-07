/**
 * Created by berne on 26/05/15.
 */
/* snMonitor.scala */

import org.apache.spark.SparkContext._
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.rdd.RDD
import org.apache.spark.api.java.function.Function2
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import scala.collection.JavaConversions._

object snMonitor {
  def main(args: Array[String]) {
    val HISTORICINTERVAL = 10
    val RECENTICINTERVAL= 5
    val Array(consumerKey, consumerSecret, accessToken, accessTokenSecret) = args.take(4)
    val dictfilenm = args(4)
    val outdir = args(5)
    //val filters = args.takeRight(args.length - 4)

    // Set the system properties so that Twitter4j library used by twitter stream
    // can use them to generat OAuth credentials
    System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)
    val dictCompany = new Company(dictfilenm)
    val filters = dictCompany.getTermsArray
    val conf = new SparkConf()
      .setMaster("local[3]")
      .setAppName("snMonitor Application")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(15))
    
    //Stub Configurations
    val stubstream = ssc.socketTextStream("localhost", 9999)
    val stubcollector = stubstream.map(text => (dictCompany.getName,text))
    //Twitter Configurations
    val twstream = TwitterUtils.createStream(ssc, None, filters)
    val twcollector = twstream.map(status => (dictCompany.getName,status.getText))


    //unifiedStream aggregates all input streams
   // val unifiedStream = tw_txt.union()
    val unifiedStream = twcollector.union(stubcollector)
    val words = unifiedStream.flatMapValues(_.toLowerCase.split("[^a-zA-Z0-9@#-]"))
    words.print()
    //gets categories
    //val wordCategories = dictCompany.wordCategory()
    unifiedStream.print()
    val pairs = words.map({case (company,word) => ((company,word), 1.0)})
    val categories = words.flatMap({case (company, word) => (dictCompany.wordCategory(word)).map(cat => ((company, cat),1.0))})

    //Gets tuples with a 0.0 for each category
    val catTuples = dictCompany.categoryTuples()

    //Converts from Java to Scala format
    var list = List[Tuple2[Tuple2[String, String], Double]]()
    for (tup <- catTuples) {
      var scalaTup = ((tup._1._1, tup._1._2), tup._2.doubleValue)
      list = scalaTup :: list
    }
    //Transforms the list of tuples into an RDD
    val zerosRDD = sc.parallelize(list)

    //Creates a union between categories and zerosRDD
    val categoriesWithZeros = categories.transform(rdd => rdd.union(zerosRDD))

    val categoryCounts = categoriesWithZeros.reduceByKey(_+_)
    
    val countWriter = new CountWriter(outdir)
    categoryCounts.foreachRDD((rdd,time) =>  countWriter.appendCategoryCount(rdd.collect(),time))
    
    val categoryCountsHistoricWindow = categoryCounts.window(Minutes(HISTORICINTERVAL), Seconds(15)).groupByKey
    val categoryCountsRecentWindow = categoryCounts.window(Minutes(RECENTICINTERVAL), Seconds(15)).groupByKey
    val historicStat = categoryCountsHistoricWindow.mapValues( value => org.apache.spark.util.StatCounter(value))
    val recentStat = categoryCountsRecentWindow.mapValues( value => org.apache.spark.util.StatCounter(value))
    val stats = historicStat.join(recentStat)
    //stats.print()

    //val wordCounts = pairs.reduceByKey(_+_)
    //val wordCountsHistoricWindow = wordCounts.window(Minutes(HISTORICINTERVAL), Seconds(15)).groupByKey
    //val wordCountsRecentWindow = wordCounts.window(Minutes(RECENTICINTERVAL), Seconds(15)).groupByKey
    //val historicStat = wordCountsHistoricWindow.mapValues( value => org.apache.spark.util.StatCounter(value))
    //val recentStat = wordCountsRecentWindow.mapValues( value => org.apache.spark.util.StatCounter(value))
    //val stats = historicStat.join(recentStat)

    // Print the first ten elements of each RDD generated in this DStream to the console
    //wordCounts.print()
    //unifiedStream.print()
    //historicStat.print()
    //recentStat.print()
    //val s = stats.foreachRDD(value =>print(value))
    stats.foreachRDD(rdd => rdd.foreach(rdd => println(rdd.toString())))
    ssc.start()
    ssc.awaitTermination()
  }
}