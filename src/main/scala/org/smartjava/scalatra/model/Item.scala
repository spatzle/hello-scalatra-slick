package org.smartjava.scalatra.model

case class Item(
    name:String,
    id: Number,
    startPrice: Number,
    currency: String,
    description: String,
    links: List[Link]
);
 