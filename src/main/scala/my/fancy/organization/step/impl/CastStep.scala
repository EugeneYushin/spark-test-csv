package my.fancy.organization.step.impl

import com.typesafe.scalalogging.LazyLogging
import my.fancy.organization.beans.Request
import my.fancy.organization.step.Step
import org.apache.spark.sql.DataFrame
import org.json4s.MappingException
import org.json4s.jackson.JsonMethods

import scala.util.{Failure, Success, Try}


class CastStep(json: String) extends Step with LazyLogging {
  implicit val formats = org.json4s.DefaultFormats

  private val rules = parseRequest()

  override def run(df: DataFrame): DataFrame = {
    logger.info("----------------")
    logger.info("Run CAST")
    df.selectExpr(
      rules.map(rule => {
        val expr = rule.date_expression match {
          case Some(format) => s"CAST(CAST(UNIX_TIMESTAMP(${rule.existing_col_name}, '$format') AS TIMESTAMP) AS ${rule.new_data_type}) AS ${rule.new_col_name}"
          case None => s"CAST(${rule.existing_col_name} AS ${rule.new_data_type}) AS ${rule.new_col_name}"
        }

        logger.debug(s"Select expression: $expr")
        expr
      }): _*
    )
  }

  private def parseRequest(): List[Request] = {
    Try(JsonMethods.parse(json).extract[List[Request]]) match {
      case Success(x) => x
      case Failure(e) => e match {
        case me: MappingException =>
          logger.error("Can't parse input request. List of entries is expected!")
          throw e
        case _ => throw e
      }
    }
  }
}
