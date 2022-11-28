package spark

import java.util.concurrent.atomic.AtomicLong

import com.alibaba.fastjson.JSON
import org.apache.spark.SparkConf
import org.apache.spark.streaming.api.java.JavaDStream.fromDStream
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}



object StreamSort {
  def main(args: Array[String]): Unit = {
    //1.初始化 Spark 配置信息
    val sparkConf = new
        SparkConf().setMaster("local[*]").setAppName("StreamSort")
    //2.初始化 SparkStreamingContext
    val ssc = new StreamingContext(sparkConf,Seconds(10))
    //3.通过监控端口创建 DStream，读进来的数据为一行行
    val texts: DStream[String] = ssc.textFileStream("input2")
    val topNCounter:AtomicLong=new AtomicLong(10);
    // val wordAndOneStreams = texts.map((o:String)=>{JSON.parseArray(o,new ProjectJava1().getClass).get(0)})
    val wordAndOneStreams = texts.flatMap((o:String)=>{JSON.parseArray(o,new ProjectJava1().getClass).toArray}).map(p=>p.asInstanceOf[ProjectJava1])
      .map(_.compute()).transform(rdd =>{
      rdd.sortBy(p => p.getPullRequests,ascending = false);
    }).filter(o=>topNCounter.decrementAndGet()>=0).map(o=>JSON.toJSONString(o,true)).saveAsTextFiles("output3")

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

    //    wordAndOneStreams.saveAsTextFiles("outputspark")
    ssc.start()
    ssc.awaitTermination()
  }
}
