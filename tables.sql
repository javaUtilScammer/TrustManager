CREATE TABLE Accounts(
	account_id int AUTO_INCREMENT,
	username varchar(15) UNIQUE NOT NULL,
	password varchar(15) NOT NULL,
	birthdate timestamp NOT NULL, 
	created_at timestamp NOT NULL, 
	last_updated_at timestamp NOT NULL,
	e_mail_address varchar(25) UNIQUE NOT NULL, 
	trust_rating double NOT NULL, 
	last_lat double NOT NULL,
	last_lng double NOT NULL,

	PRIMARY KEY(account_id)
);


CREATE TABLE Contributions(
	contribution_id int AUTO_INCREMENT,
	contributor_id int NOT NULL, 
	message varchar(150) NOT NULL,
	contribution_score double NOT NULL, 
	data_array varchar(150) NOT NULL,
	created_at timestamp NOT NULL,
	state int(4) NOT NULL,

	PRIMARY KEY(contribution_id),
	FOREIGN KEY(contributor_id) REFERENCES Accounts(account_id)
		ON DELETE CASCADE
);

CREATE TABLE Evaluations(
	evaluation_id int AUTO_INCREMENT,
	message varchar(150) NOT NULL,
	rating double NOT NULL, 
	created_at timestamp NOT NULL,
	created_by int NOT NULL,
	contribution_id int NOT NULL, 

	PRIMARY KEY(evaluation_id),
	FOREIGN KEY(created_by) REFERENCES Accounts(account_id)
		ON DELETE CASCADE,
	FOREIGN KEY(contribution_id) REFERENCES Contributions(contribution_id)
		ON DELETE CASCADE
);

CREATE TABLE Comments(
	comment_id int AUTO_INCREMENT,
	message varchar(150) NOT NULL,
	created_at timestamp NOT NULL,
	account_id int NOT NULL, 
	evaluation_id int NOT NULL, 

	PRIMARY KEY(comment_id),
	FOREIGN KEY(account_id) REFERENCES Accounts(account_id)
		ON DELETE CASCADE,
	FOREIGN KEY(evaluation_id) REFERENCES Evaluations(evaluation_id)
		ON DELETE CASCADE
);