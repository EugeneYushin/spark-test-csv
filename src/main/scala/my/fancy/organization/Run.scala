package my.fancy.organization

import java.io.File

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import my.fancy.organization.step.impl.{CastStep, FilterStep, StatsStep}
import my.fancy.organization.wf.Workflow
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object Run extends LazyLogging {
  def main(args: Array[String]): Unit = {
    logger.info("Start App")

    val file = args(0)
    val conf = ConfigFactory.parseFile(new File(args(1)))

    val castJson = conf.getString("cast")

    logger.info(s"File to process: $file")
    logger.debug(s"Cast request: $castJson")


    val sparkConf = new SparkConf()
      .setAppName("spark-test-csv")
      .setMaster("local[2]")
    val session = SparkSession.builder().config(sparkConf).getOrCreate()


    val inputDf = session.read
      .options(Utils.confToMap(conf.getConfig("input.options")))
      .csv(file)

    logger.info("Input sample")
    inputDf.show()

    val wf = Workflow(conf)
      .add(new FilterStep)
      .add(new CastStep(castJson))
      .add(new StatsStep)

    val res = wf.run(inputDf)

    logger.info("Final results:")
    res.show(Int.MaxValue - 1, truncate = false)

    logger.info("Finished")
  }
}
