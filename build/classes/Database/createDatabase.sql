CREATE TABLE scores(
	id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
	player varchar (20) NOT NULL,
	map varchar (20) NOT NULL,
	score INT NOT NULL,
	PRIMARY KEY (id)
);