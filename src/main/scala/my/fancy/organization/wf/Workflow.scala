package my.fancy.organization.wf

import com.typesafe.config.Config
import my.fancy.organization.step.Step
import org.apache.spark.sql.DataFrame

import scala.collection.mutable.ListBuffer

/**
  * Workflow builder
  */
class Workflow(conf: Config) {
  private val wf = ListBuffer[Step]()

  /**
    * Add Step
    * @param step Step
    * @return this
    */
  def add(step: Step): this.type = {
    wf += step
    this
  }

  /**
    * Submit workflow
    * @param df Initial dataframe
    * @return Dataframe referred to results of last processed Step
    */
  @throws[IndexOutOfBoundsException]
  def run(df: DataFrame): DataFrame = {
    var counter = wf.size
    if (wf.nonEmpty) wf.foldLeft[DataFrame](df)((df, step) => {
      val output = step.run(df)
      if (conf.getBoolean("flow.intermediateResults.show") && counter > 1)
        output.show(conf.getInt("flow.intermediateResults.count"), truncate = false)
      output.printSchema()

      counter -= 1
      output
    }) else throw new IndexOutOfBoundsException("Empty Workflow")
  }
}

object Workflow {
  def apply(conf: Config): Workflow = new Workflow(conf)
}
