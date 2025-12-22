CREATE TABLE Users (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(250),
    password_hash VARCHAR(250) NOT NULL,
    role user_role NOT NULL DEFAULT 'USER'
);

CREATE TABLE Contestant (
    contestant_id BIGSERIAL PRIMARY KEY,
    contestant_name VARCHAR(100),
    profession VARCHAR(100),
    age INT,
    studies studies_type,
    notes TEXT
);

CREATE TABLE Presenter (
    presenter_id BIGSERIAL PRIMARY KEY,
    presenter_name VARCHAR(100)
);

CREATE TABLE Contest (
    contest_id BIGSERIAL PRIMARY KEY,
    title VARCHAR(250),
    description TEXT,
    start_date_time TIMESTAMP,
    end_date_time TIMESTAMP,
    prize INT
);