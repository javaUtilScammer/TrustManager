CREATE TABLE Accounts(
	account_id int AUTO_INCREMENT,
	username varchar(15) UNIQUE NOT NULL,
	created_at timestamp NOT NULL, 
	last_updated_at timestamp NOT NULL,
	trust_rating double NOT NULL, 
	trust_validity double NOT NULL,
	PRIMARY KEY(account_id)
);


CREATE TABLE Contributions(
	contribution_id int AUTO_INCREMENT,
	contributor_id int NOT NULL, 
	contribution_score double NOT NULL, 
	score_validity double NOT NULL, 
	created_at timestamp NOT NULL,
	state int(4) NOT NULL,

	PRIMARY KEY(contribution_id),
	FOREIGN KEY(contributor_id) REFERENCES Accounts(account_id)
		ON DELETE CASCADE
);

CREATE TABLE Evaluations(
	evaluation_id int AUTO_INCREMENT,
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