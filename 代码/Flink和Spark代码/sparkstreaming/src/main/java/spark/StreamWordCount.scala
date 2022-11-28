package spark

import java.util.concurrent.atomic.AtomicLong

import com.alibaba.fastjson.JSON
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}



object StreamWordCount {
  def main(args: Array[String]): Unit = {
    //1.初始化 Spark 配置信息
    val sparkConf = new
        SparkConf().setMaster("local[*]").setAppName("StreamWordCount")
    //2.初始化 SparkStreamingContext
    val ssc = new StreamingContext(sparkConf,Seconds(10))
    //3.通过监控端口创建 DStream，读进来的数据为一行行
    val texts: DStream[String] = ssc.textFileStream("input1")
   // val wordAndOneStreams = texts.map((o:String)=>{JSON.parseArray(o,new ProjectJava1().getClass).get(0)})
    val wordStreams = texts.flatMap((o:String)=>{JSON.parseArray(o,new ProjectWordCount().getClass).toArray}).map(p=>p.asInstanceOf[ProjectWordCount])
      .flatMap(x=>x.getTags1.split(" "))
    //将单词映射成元组（word,1）
    val wordAndOneStreams = wordStreams.map((_, 1))
    //将相同的单词次数做统计
    val wordAndCountStreams = wordAndOneStreams.reduceByKey(_+_)
    val topNCounter:AtomicLong=new AtomicLong(1000);
    val ans = wordAndCountStreams.transform(rdd =>{
      rdd.sortBy(t => t._2, ascending = false)
    }).filter(o=>topNCounter.decrementAndGet()>=0)
    ans.repartition(1).saveAsTextFiles("outputword")
//    wordAndCountStreams.print()
//    wordAndCountStreams.saveAsTextFiles("out")
//    wordAndOneStreams.repartition(1).saveAsTextFiles("outputspark")
//    wordAndCountStreams.reduceByKey(_+_)
    //打印
//    wordAndCountStreams.saveAsTextFiles("")
//    var ans = wordAndCountStreams.repartition(1)
//    ans.s()
//    for ()
  //  val wordAndOneStreams = texts.flatMap((o:String)=>{JSON.parseArray(o,ProjectJava.getClass).toArray})
    //  .map(p=>p.asInstanceOf[ProjectJava.type ])
     // .map(_.compute())
//    //将每一行数据做切分，形成一个个单词
//    val wordStreams =texts.flatMap(_.split(" "))
//    //将单词映射成元组（word,1）
//    val wordAndOneStreams = wordStreams.map((_, 1))li
//    //将相同的单词次数做统计
//    val wordAndCountStreams = wordAndOneStreams.reduceByKey(_+_)
//    //打印
//    wordAndCountStreams.print()
    //启动 SparkStreamingContext
//    wordAndOneStreams.print();

//    wordAndOneStreams.repartition(1).saveAsTextFiles("outputspark")
    ssc.start()
    ssc.awaitTermination()
  }
}
