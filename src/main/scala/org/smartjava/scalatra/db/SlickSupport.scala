package org.smartjava.scalatra.db
import org.scalatra.ScalatraServlet
import org.slf4j.LoggerFactory
import java.util.Properties
import com.mchange.v2.c3p0.ComboPooledDataSource
import scala.slick.driver.MySQLDriver.simple._
import Database.threadLocalSession

trait SlickSupport extends ScalatraServlet {
  val logger = LoggerFactory.getLogger(getClass)
  val cpds = {
    val props = new Properties
    props.load(getClass.getResourceAsStream("/c3p0.properties"))
    val cpds = new ComboPooledDataSource
    cpds.setProperties(props)
    logger.info("Created c3p0 connection pool")
    cpds
  }
  def closeDbConnection() {
    logger.info("Closing c3po connection pool")
    cpds.close
  }
  val db = Database.forDataSource(cpds)
  override def destroy() {
    super.destroy()
    closeDbConnection
  }
}