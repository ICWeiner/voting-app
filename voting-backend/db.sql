CREATE DATABASE joker;

-- Drop tables

DROP TABLE IF EXISTS Bet CASCADE;
DROP TABLE IF EXISTS ContestContestant CASCADE;
DROP TABLE IF EXISTS ContestPresenter CASCADE;
DROP TABLE IF EXISTS Contest CASCADE;
DROP TABLE IF EXISTS Contestant CASCADE;
DROP TABLE IF EXISTS Presenter CASCADE;
DROP TABLE IF EXISTS Users CASCADE;

-- Drop ENUM types

DROP TYPE IF EXISTS user_role CASCADE;
DROP TYPE IF EXISTS studies_type CASCADE;

-- ENUM types

CREATE TYPE user_role AS ENUM ('USER', 'ADMIN');
CREATE TYPE studies_type AS ENUM ('NONE', 'PRIMARY', 'SECONDARY', 'BACHELOR', 'MASTER', 'PHD');

-- Tables

CREATE TABLE Users (
    userId SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(250),
    passwordHash VARCHAR(250) NOT NULL,
    role user_role NOT NULL DEFAULT 'USER'
);

CREATE TABLE Contestant (
    contestantId SERIAL PRIMARY KEY,
    contestantName VARCHAR(100),
    profession VARCHAR(100),
    age INT,
    studies studies_type,
    notes TEXT
);

CREATE TABLE Presenter (
    presenterId SERIAL PRIMARY KEY,
    presenterName VARCHAR(100)
);

CREATE TABLE Contest (
    contestId SERIAL PRIMARY KEY,
    title VARCHAR(250),
    description TEXT,
    startDateTime TIMESTAMP,
    endDateTime TIMESTAMP,
    prize INT,
    contestantId INT NOT NULL REFERENCES Contestant(contestantId)
);

CREATE TABLE Bet (
    betId SERIAL PRIMARY KEY,
    amount INT NOT NULL,
    betDateTime TIMESTAMP,
    userId INT NOT NULL REFERENCES Users(userId),
    contestId INT NOT NULL REFERENCES Contest(contestId)
);

CREATE TABLE ContestContestant (
    contestId INT NOT NULL REFERENCES Contest(contestId),
    contestantId INT NOT NULL REFERENCES Contestant(contestantId),
    isSuperJoker BOOLEAN NOT NULL,
    PRIMARY KEY (contestId, contestantId)
);

CREATE TABLE ContestPresenter (
    contestantId INT NOT NULL REFERENCES Contestant(contestantId),
    presenterId INT NOT NULL REFERENCES Presenter(presenterId),
    notes TEXT,
    PRIMARY KEY (contestantId, presenterId)
);