package org.smartjava.scalatra.repository

import org.smartjava.scalatra.model._
import org.smartjava.scalatra.model.Coffee._
import scala.slick.session._
import scala.slick.lifted._
import com.mchange.v2.c3p0.ComboPooledDataSource
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.driver.MySQLDriver.simple.Database
import Database.threadLocalSession

class CoffeeRepository extends RepositoryBase {

  val supplierRepo = new SupplierRepository;
  
  // Definition of the COFFEES table
  object Coffees extends Table[(String, Int, Double, Int, Int)]("COFFEES") {
    def name = column[String]("COF_NAME", O.PrimaryKey)
    def supID = column[Int]("SUP_ID")
    def price = column[Double]("PRICE")
    def sales = column[Int]("SALES")
    def total = column[Int]("TOTAL")
    def * = name ~ supID ~ price ~ sales ~ total

    def noID = name ~ supID ~ price ~ sales ~ total
    // A reified foreign key relation that can be navigated to create a join
    def supplier = foreignKey("SUP_FK", supID, supplierRepo.Suppliers)(_.id)
  }

  /*
	get("/coffees") {
    db withSession {
      val q3 = for {
        c <- coffeeRepo.Coffees
        s <- c.supplier
      } yield (c.name.asColumnOf[String], s.name.asColumnOf[String])
      contentType = "text/html"
      q3.list.map { case (s1, s2) => "  " + s1 + " supplied by " + s2 } mkString "<br />"
    }
  }*/
  def create(coffee: Coffee) = {
    db withSession {
    	val res = Coffees.insert(
        coffee.name,
        coffee.supID.intValue,
        coffee.price.doubleValue,
        coffee.sales.intValue,
        coffee.total.intValue);
    }
    // create a bid to return
  }

  def get_price_filter_range(c_search: CoffeeSearch): List[CoffeeWithSupplier] = {
//    var result: String= "";
    var l4: List[CoffeeWithSupplier]= List[CoffeeWithSupplier]()
    db withSession {
      val q3 = for {
        c <- Coffees if (c.price > c_search.min_price && c.price < c_search.max_price)
        s <- c.supplier
      } yield (c.name, s.name)
//      val l3: List[(String, String)] = q3.list
//      for ((s1, s2)<- l3) (
      for ((s1, s2)<- q3.list) (
          l4  ::= CoffeeWithSupplier(s1,s2) 
      )   
          //result += "  " + s1 + " supplied by " + s2 + "<br />")//  result += q3.selectStatement  
    }
    l4
  }

}