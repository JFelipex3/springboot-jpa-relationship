CREATE TABLE clients (
  id bigint NOT NULL AUTO_INCREMENT,
  lastname varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE invoices (
  client_id bigint DEFAULT NULL,
  id bigint NOT NULL AUTO_INCREMENT,
  total bigint DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY fk_client_id (client_id),
  CONSTRAINT fk_client_id FOREIGN KEY (client_id) REFERENCES clients (id)
);

CREATE TABLE addresses (
  id bigint NOT NULL AUTO_INCREMENT,
  street varchar(255) DEFAULT NULL,
  number int DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE clients_addresses (
  addresses_id bigint NOT NULL,
  client_id bigint NOT NULL,
  UNIQUE KEY pk_client_addresses_id (addresses_id),
  KEY fk_client_id (client_id),
  CONSTRAINT fk_client_id FOREIGN KEY (client_id) REFERENCES clients (id),
  CONSTRAINT fk_addresses_id FOREIGN KEY (addresses_id) REFERENCES addresses (id)
)

insert into clients (name, lastname) values ('Pepe', 'Doe');
insert into clients (name, lastname) values ('Maria', 'Roe');