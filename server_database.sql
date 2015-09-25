CREATE DATABASE IF NOT EXISTS ClientServers;
USE ClientServers;

CREATE TABLE Clients(
	client_id int AUTO_INCREMENT,
	key varchar(35) UNIQUE NOT NULL,
	validation_time int NOT NULL, 
	default_score double NOT NULL, 
	rating_scale double NOT NULL
);
