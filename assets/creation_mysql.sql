CREATE TABLE sc_user
(
  id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255),
  firstname VARCHAR(255),
  lastname VARCHAR(255)
);

CREATE TABLE sc_item
(
  id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  price DECIMAL(10, 2),
  currency VARCHAR(255),
  description VARCHAR(255),
  owner INT unsigned ,
  FOREIGN KEY (owner) REFERENCES sc_user(id)
  	ON DELETE CASCADE
);

CREATE TABLE sc_bid
(
   id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY
  , item INT UNSIGNED
  , min DECIMAL(10, 2)
  , max DECIMAL(10, 2)
  , currency VARCHAR(255)
  , bidder INT UNSIGNED
  , date DATETIME
  , FOREIGN KEY (item) REFERENCES sc_item (id)
  , FOREIGN KEY (bidder) REFERENCES sc_user (id)
);
 
DROP TABLE sc_bid;
DROP TABLE sc_item;
DROP TABLE sc_user;
