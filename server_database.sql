CREATE DATABASE IF NOT EXISTS ClientServers;
USE ClientServers;

CREATE TABLE Clients(
	client_id int AUTO_INCREMENT,
	client_name varchar(35) NOT NULL, 
	client_key varchar(35) UNIQUE NOT NULL,
	validation_type varchar (20) NOT NULL,
	validation_time int NOT NULL, 
	default_score double NOT NULL, 
	rating_scale double NOT NULL,
	degree_of_strictness double NOT NULL, 
	beta_factor double NOT NULL, 
	active_user_time double NOT NULL,
	active_evaluation_time double NOT NULL,

	PRIMARY KEY(client_id)
);
