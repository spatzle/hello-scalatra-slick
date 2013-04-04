package org.smartjava.scalatra.model.Coffee

case class CoffeeSearch(
	name: String = "N/A"
	,min_price: Double = -1.00
	,min_sales: Number = -1
	,min_total: Number = -1
	,max_price: Double = 5000.00
	,max_sales: Number = 5000
	,max_total: Number = 5000
);