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

CREATE TABLE user_roles(
user_id INT NOT NULL,
role_id INT NOT NULL,
PRIMARY KEY (user_id)
)

CREATE TABLE allowances(
id INT NOT NULL AUTO_INCREMENT,
allowance_amount double,
allotted_by INT,
allotted_to INT,
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

