package org.smartjava.scalatra.repository
// simple implementation of the bidrepository

import org.smartjava.scalatra.model.Bid

import scala.slick.session._
import scala.slick.lifted.TypeMapper._
//import scala.slick.driver.PostgresDriver._
import scala.slick.driver.MySQLDriver.simple._

class SupplierRepository extends RepositoryBase {

  // Definition of the SUPPLIERS table
  object Suppliers extends Table[(Int, String, String, String, String, String)]("SUPPLIERS") {
    def id = column[Int]("SUP_ID", O.PrimaryKey, O.AutoInc) // This is the primary key column
    def name = column[String]("SUP_NAME")
    def street = column[String]("STREET")
    def city = column[String]("CITY")
    def state = column[String]("STATE")
    def zip = column[String]("ZIP")

    // Every table needs a * projection with the same type as the table's type parameter
    def * = id ~ name ~ street ~ city ~ state ~ zip
    def autoInc = name ~ street ~ city ~ state ~ zip returning id
  }
}