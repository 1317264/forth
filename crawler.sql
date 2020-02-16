CREATE DATABASE crawler;
USE crawler;

CREATE TABLE novel
( 
id INT UNIQUE NOT NULL,
title VARCHAR(255),
writer VARCHAR(255),
introduction TEXT,
cover BLOB
);

CREATE TABLE title
(
id INT NOT NULL,
chapter INT,
title VARCHAR(255),
UNIQUE(id,chapter)
);

CREATE TABLE content
(
id INT NOT NULL,
chapter INT,
content MEDIUMTEXT,
UNIQUE(id,chapter)
);


CREATE TABLE movie
(
id INT UNIQUE NOT NULL,
cover BLOB,
title varchar(255),
time varchar(10),
score FLOAT,
scorenum INT,
5star VARCHAR(6),
4star VARCHAR(6),
3star VARCHAR(6),
2star VARCHAR(6),
1star VARCHAR(6)
);

CREATE TABLE director
(
id INT NOT NULL,
num INT,
director varchar(255),
UNIQUE(id,num)
);

CREATE TABLE screenwriter
(
id INT NOT NULL,
num INT,
screenwriter varchar(255),
UNIQUE(id,num)
);

CREATE TABLE actor
(
id INT NOT NULL,
num INT,
actor varchar(255),
UNIQUE(id,num)
);