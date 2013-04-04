package org.smartjava.scalatra.repository
import scala.slick.session.Database
import Database.threadLocalSession

trait RepositoryBase {
  val db = 
    Database.forURL("jdbc:mysql://localhost/test?user=root&password=", driver = "com.mysql.jdbc.Driver")
}