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
  KEY FK_client_id (client_id),
  CONSTRAINT FK_client_id FOREIGN KEY (client_id) REFERENCES clients (id)
);

insert into clients (name, lastname) values ('Pepe', 'Doe');
insert into clients (name, lastname) values ('Maria', 'Roe');