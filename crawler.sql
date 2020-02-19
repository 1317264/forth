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

CREATE TABLE chapter
(
id INT NOT NULL,
chapter INT,
title VARCHAR(255),
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
star5 VARCHAR(6),
star4 VARCHAR(6),
star3 VARCHAR(6),
star2 VARCHAR(6),
star1 VARCHAR(6)
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
