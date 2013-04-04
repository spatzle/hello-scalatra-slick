package org.smartjava.scalatra.model

case class Bid(
    id: Option[Long],
    forItem: Number,
    minimum: Number,
    maximum: Number,
    currency: String,
    bidder: Long,
    date: Long
);