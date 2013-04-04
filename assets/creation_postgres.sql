CREATE TABLE sc_user
(
  id serial NOT NULL,
  username text,
  firstname text,
  lastname text,
  CONSTRAINT sc_user_pkey PRIMARY KEY (id )
);
CREATE SEQUENCE sc_bid_id_seq1 START 123;
CREATE SEQUENCE sc_bid_id_seq START 234;

CREATE TABLE sc_item
(
  id integer NOT NULL DEFAULT nextval('sc_bid_id_seq'::regclass),
  name text,
  price numeric,
  currency text,
  description text,
  owner integer,
  CONSTRAINT sc_bid_pkey PRIMARY KEY (id ),
  CONSTRAINT sc_bid_owner_fkey FOREIGN KEY (owner)
      REFERENCES sc_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
 

CREATE TABLE sc_bid
(
   id integer NOT NULL DEFAULT nextval('sc_bid_id_seq1'::regclass),
  
  "for" integer,
  min numeric,
  max numeric,
  currency text,
  bidder integer,
  date numeric,
  CONSTRAINT sc_bid_pkey1 PRIMARY KEY (id ),
  CONSTRAINT sc_bid_bidder_fkey FOREIGN KEY (bidder)
      REFERENCES sc_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT sc_bid_for_fkey FOREIGN KEY ("for")
      REFERENCES sc_item (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
 
 
 drop table sc_bid;
drop table sc_user;
drop SEQUENCE sc_bid_id_seq1;
drop table sc_item;



 