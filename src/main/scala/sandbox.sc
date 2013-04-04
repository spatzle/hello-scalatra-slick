object test {
  import scala.slick.session.Database
  import scala.slick.driver.MySQLDriver.simple._
  import scala.slick.session.Database._
  import org.slf4j.LoggerFactory
  import scala.slick.lifted.BaseTypeMapper
  import scala.slick.driver.BasicProfile
  import scala.slick.session.{ PositionedParameters, PositionedResult }
  import language.implicitConversions

  object Sandbox {
    def database = {
      //    forURL("jdbc:postgresql://localhost/test?user=postgres&password=dupIsZr0", driver = "org.postgresql.Driver")
      forURL("jdbc:mysql://localhost:3306/test", driver = "com.mysql.jdbc.Driver")
    }
  }
  object Supplier extends Table[(String, String, Int, String)]("supplier") {
    def snum = column[String]("snum")
    def sname = column[String]("sname")
    def status = column[Int]("status")
    def city = column[String]("city")
    def * = snum ~ sname ~ status ~ city
  }
  println("Hello, world!")                        //> Hello, world!
  val session = Sandbox.database.createSession    //> session  : scala.slick.session.Session = scala.slick.session.BaseSession@727
                                                  //| b7bee
  val query = tableToQuery(Supplier)              //> query  : scala.slick.lifted.Query[test.Supplier.type,scala.slick.lifted.Not
                                                  //| hingContainer#TableNothing] = scala.slick.lifted.NonWrappingQuery@24e55d75
                                                  //| 
  println(query.selectStatement)                  //> SLF4J: Class path contains multiple SLF4J bindings.
                                                  //| SLF4J: Found binding in [jar:file:/C:/Users/joyce.chan/.ivy2/cache/org.slf4
                                                  //| j/slf4j-nop/jars/slf4j-nop-1.6.4.jar!/org/slf4j/impl/StaticLoggerBinder.cla
                                                  //| ss]
                                                  //| SLF4J: Found binding in [jar:file:/C:/Users/joyce.chan/.ivy2/cache/ch.qos.l
                                                  //| ogback/logback-classic/jars/logback-classic-1.0.6.jar!/org/slf4j/impl/Stati
                                                  //| cLoggerBinder.class]
                                                  //| SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explana
                                                  //| tion.
                                                  //| SLF4J: Actual binding is of type [org.slf4j.helpers.NOPLoggerFactory]
                                                  //| select x2.`snum`, x2.`sname`, x2.`status`, x2.`city` from `supplier` x2
  println("first: " + query.first()(session))     //> first: (S1,Smith,20,London)
  //    query.foreach( println(_) )(session)

  val pquery = tableToQuery(Supplier) map (s => s.snum ~ s.sname)
                                                  //> pquery  : scala.slick.lifted.Query[scala.slick.lifted.Projection2[String,St
                                                  //| ring],(String, String)] = scala.slick.lifted.WrappingQuery@5d003b
  println(pquery.selectStatement)                 //> select x2.`snum`, x2.`sname` from `supplier` x2

  // choose columns
  Supplier.map(s => s.snum ~ s.sname).foreach(println(_))(session)
                                                  //> (S1,Smith)
                                                  //| (S2,Jones)
                                                  //| (S3,Blake)
                                                  //| (S4,Clark)
                                                  //| (S5,Adams)
  println()                                       //> 

  // where clause
  Supplier.filter(_.city === "London").map(s => s.snum ~ s.sname).foreach(println(_))(session)
                                                  //> (S1,Smith)
  println()                                       //> 

  // updates
  val uquery = Supplier.filter(p => p.sname === "Clark" || p.sname === "Adams").map(_.city)
                                                  //> uquery  : scala.slick.lifted.Query[scala.slick.lifted.Column[String],String
                                                  //| ] = scala.slick.lifted.WrappingQuery@3c41a9ce
  println(uquery.updateStatement)                 //> update `supplier` set `city` = ? where (`supplier`.`sname` = 'Clark') or (`
                                                  //| supplier`.`sname` = 'Adams')
  uquery.update("Rome")(session)                  //> res0: Int = 2
  tableToQuery(Supplier).foreach(println(_))(session)
                                                  //> (S1,Smith,20,London)
                                                  //| (S2,Jones,10,Paris)
                                                  //| (S3,Blake,30,Paris)
                                                  //| (S4,Clark,20,Rome)
                                                  //| (S5,Adams,30,Rome)
  println()                                       //> 

  //    insert
  //    val newSid = Sid("S6")
  val projection = Supplier.snum ~ Supplier.city ~ Supplier.sname ~ Supplier.status
                                                  //> projection  : scala.slick.lifted.Projection4[String,String,String,Int] = Pr
                                                  //| ojection4
  println(projection.insertStatement)             //> INSERT INTO `supplier` (`snum`,`city`,`sname`,`status`) VALUES (?,?,?,?)
  //    projection.insert(newSid, "Berlin", "Mills", 30)(session)
  projection.insert("S6", "Berlin", "Mills", 30)(session)
                                                  //> res1: Int = 1
  //    Supplier.filter(_.snum === Sid("S6")).map(_.snum).first()(session)
  Supplier.filter(_.snum === "S6").map(_.snum).first()(session)
                                                  //> res2: String = S6
  //    tableToQuery(Supplier).foreach(println(_))(session)
  println()                                       //> 

  //     delete
  val dquery = Supplier.filter(_.snum === "S6")   //> dquery  : scala.slick.lifted.Query[test.Supplier.type,scala.slick.lifted.No
                                                  //| thingContainer#TableNothing] = scala.slick.lifted.WrappingQuery@11e35aae
  println(dquery.deleteStatement)                 //> delete from `supplier` where `supplier`.`snum` = 'S6'
  dquery.delete(session)                          //> res3: Int = 1
  tableToQuery(Supplier).foreach(println(_))(session)
                                                  //> (S1,Smith,20,London)
                                                  //| (S2,Jones,10,Paris)
                                                  //| (S3,Blake,30,Paris)
                                                  //| (S4,Clark,20,Rome)
                                                  //| (S5,Adams,30,Rome)
  println("end")                                  //> end
  session.close()
}