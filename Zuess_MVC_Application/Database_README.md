References: 
https://stackoverflow.com/questions/4169893/is-it-good-database-design-to-have-admin-users-in-the-same-table-as-front-end-us

Table Creation Scripts
---

USE zuess_web; #database name


CREATE TABLE users(
id INT NOT NULL AUTO_INCREMENT,
email VARCHAR(50),
password VARCHAR(100),
first_name VARCHAR(50),
last_name VARCHAR(50),
eth_account_id VARCHAR(60) NULL,
eth_account_balance double NULL,
organization_id INT NULL,
PRIMARY KEY (id)
)

CREATE TABLE roles(
role_id INT NOT NULL AUTO_INCREMENT,
role_name VARCHAR(20),
PRIMARY KEY (role_id)
)

CREATE TABLE user_roles (
  id INT NOT NULL,
  role_id INT NOT NULL,
  KEY user_fk_idx (id),
  KEY role_fk_idx (role_id),
  CONSTRAINT role_fk FOREIGN KEY (role_id) REFERENCES roles (role_id),
  CONSTRAINT user_fk FOREIGN KEY (id) REFERENCES users (id)
);

CREATE TABLE scholarships (
   id INT NOT NULL AUTO_INCREMENT,
    recipient_id INT,
    recipient_eth_id VARCHAR(60),
    amount int,
    date_granted DATE NULL,
    date_expires DATE NULL,
    PRIMARY KEY (id)
);

CREATE TABLE InventoryItem (
   id INT NOT NULL AUTO_INCREMENT,
    scholarship_eligible boolean,
    name VARCHAR(50),
    type VARCHAR(50) NULL,
    description VARCHAR(50) NULL,
    price int, 
    image_source VARCHAR(100) NULL,
    PRIMARY KEY (id)
);

CREATE TABLE store_transactions(
   id INT NOT NULL AUTO_INCREMENT,
   user_id INT,
   transaction_total INT,
   scholarship_funds_used INT,
   items_list VARCHAR(500),
   transaction_date DATE,
   PRIMARY KEY (id)
)

CREATE TABLE transactions(
id INT NOT NULL AUTO_INCREMENT,
transaction_hash VARCHAR(50),
transaction_index VARCHAR(50),
from_account INT,
to_account INT,
gas_used INT,
event_type VARCHAR(50),
PRIMARY KEY (id)
)

@Insert Statements
INSERT INTO InventoryItem (scholarship_eligible, name, price, image_source)
VALUES (false, "CSUMB Adidas Logo T-Shirt", 12, "images/AdidasShirt.png"),
(false, "CSUMB Otters Crew Sweatshirt", 38, "images/OtterCrew.png"),
(false, "CSUMB Otters Hooded Sweatshirt", 45, "images/HoodSweat.png"),
(false, "CSUMB Dad T-Shirt", 19, "images/OtterTee.png"),
(false, "CSUMB Otters Hat", 24, "images/OtterHat.png"),
(false, "CSUMB Adjustable Cap", 22, "images/ZypherHat.png"),
(true, "Database Book", 70, "images/DatabaseBook.png"),
(true, "Machine Learning Book", 60, "images/MachineBook.png"),
(true, "Coding Interview Book", 50, "images/Codingbook.png")

