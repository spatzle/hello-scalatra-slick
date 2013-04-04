package org.smartjava.scalatra.repository;

import org.smartjava.scalatra.model._;

class ItemRepository {

  def get(id: Number): Option[Item] = {

    id.intValue() match {
      case 123 => {
        val l1 = new Link("application/vnd.smartbid.item", "Add item to watchlist", "/users/123/watchlist");
        val l2 = new Link("application/vnd.smartbid.bid", "Place bid on item", "/items/" + id + "/bid");
        val l3 = new Link("application/vnd.smartbid.user", "Get owner's details", "/users/123");

        val item = new Item(
          "Monty Python and the search for the holy grail",
          id,
          0.69,
          "GBP",
          "Must have item",
          List(l1, l2, l3));

        Option(item);
      };

      case _ => Option(null);
    }
  }

  def delete(item: Item) = println("deleting user: " + item)

}