package org.smartjava.scalatra

import grizzled.slf4j.Logger
import org.scalatra._
import scalate.ScalateSupport
import net.liftweb.json.compact
import net.liftweb.json.render
import net.liftweb.json.JsonDSL._
import net.liftweb.json.Serialization.{ read, write };
import org.smartjava.scalatra.repository._;
import net.liftweb.json.Serialization
import net.liftweb.json.NoTypeHints
import org.scalatra.Ok
import org.scalatra.NotFound
import org.scalatra.Created
import scala.collection.immutable.Map
import org.smartjava.scalatra.model.Coffee._

import java.util.Properties
import org.slf4j.LoggerFactory
import com.mchange.v2.c3p0.ComboPooledDataSource
import scala.slick.driver.MySQLDriver.simple._
import Database.threadLocalSession
import org.smartjava.scalatra.repository.SupplierRepository
import org.smartjava.scalatra.repository.CoffeeRepository
import org.smartjava.scalatra.db.SlickSupport


class HelloScalatraServlet extends ScalatraServlet with ScalateSupport with SlickSupport {

  // simple logger
  // val logger = Logger(classOf[HelloScalatraServlet]);
  // repo stores our items
  val itemRepo = new ItemRepository;
  val bidRepo = new BidRepository;
  val coffeeRepo = new CoffeeRepository;
  val supplierRepo = new SupplierRepository;

  // implicit value for json serialization format
  implicit val formats = Serialization.formats(NoTypeHints);

  // -----------------
  get("/db/create-tables") {
    db withSession {
      (supplierRepo.Suppliers.ddl ++ coffeeRepo.Coffees.ddl).create
    }
  }

  get("/db/load-data") {
    db withSession {
      var id1=supplierRepo.Suppliers.autoInc.insert( "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199")
      var id2=supplierRepo.Suppliers.autoInc.insert("Superior Coffee", "1 Party Place", "Mendocino", "CA", "95460")
      var id3=supplierRepo.Suppliers.autoInc.insert("The High Ground", "100 Coffee Lane", "Meadows", "CA", "93966")
      
      // Insert some coffees (using JDBC's batch insert feature, if supported by the DB)
      coffeeRepo.Coffees.insertAll(
        ("Colombian", id1, 7.99, 0, 0),
        ("French_Roast", id2, 8.99, 0, 0),
        ("Espresso", id3, 9.99, 0, 0),
        ("Colombian_Decaf", id1, 8.99, 0, 0),
        ("French_Roast_Decaf", id2, 9.99, 0, 0))
    }
  }

  get("/db/drop-tables") {
    db withSession {
      (supplierRepo.Suppliers.ddl ++ coffeeRepo.Coffees.ddl).drop
    }
  }

  get("/coffees") {
    db withSession {
      val q3 = for {
        c <- coffeeRepo.Coffees
        s <- c.supplier
      } yield (c.name.asColumnOf[String], s.name.asColumnOf[String])

      contentType = "text/html"
      q3.list.map { case (s1, s2) => "  " + s1 + " supplied by " + s2 } mkString "<br />"
    }
  }

  get("/coffee/price/range/:minprice/:maxprice") {
    val result: List[CoffeeWithSupplier] = coffeeRepo.get_price_filter_range(
      new CoffeeSearch(
        min_price = params("minprice").toDouble, max_price = params("maxprice").toDouble));
    contentType = "application/json";
    Ok(write(result));
  }

  //    get("/items/:id") {
  //    // set the result content type
  //    contentType = "application/vnd.smartbid.item+json"
  //
  //    // convert response to json and return as OK
  //    itemRepo.get(params("id").toInt) match {
  //      case Some(x) => Ok(write(x));
  //      case None => NotFound("Item with id " + params("id") + " not found");
  //    }
  //  }

  // -----------------
  get("/") {
    "hiyas!!"
  }

  //  post("/items/:id/bid", request.getContentType == "application/json") {
  //    contentType = "application/json"
  //    var createdBid = bidRepo.create(read[Bid](request.body));
  //    Created(write(createdBid), Map("Location" -> ("/users/" + createdBid.bidder + "/bids/" + createdBid.id.get)));
  //  }

  get("/users/:user/bids/:bid") {
    contentType = "application/json"
    bidRepo.get(params("bid").toInt, params("user")) match {
      case Some(x) => Ok(write(x));
      case None => NotFound("Bid with id " + params("bid") + " not found for user: " + params("user"));
    }
  }
  get("/items/:id") {
    // set the result content type
    contentType = "application/vnd.smartbid.item+json"

    // convert response to json and return as OK
    itemRepo.get(params("id").toInt) match {
      case Some(x) => Ok(write(x));
      case None => NotFound("Item with id " + params("id") + " not found");
    }
  }
}