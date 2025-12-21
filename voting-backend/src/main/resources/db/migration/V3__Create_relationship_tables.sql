CREATE TABLE Vote (
    vote_id BIGSERIAL PRIMARY KEY,
    vote_choice vote_choice NOT NULL,
    vote_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id INT NOT NULL REFERENCES Users(user_id),
    contest_id INT NOT NULL REFERENCES Contest(contest_id)
);

CREATE TABLE ContestContestant (
    contest_id INT NOT NULL REFERENCES Contest(contest_id),
    contestant_id INT NOT NULL REFERENCES Contestant(contestant_id),
    is_super_joker BOOLEAN NOT NULL,
    PRIMARY KEY (contest_id, contestant_id)
);

CREATE TABLE ContestPresenter (
    contest_id INT NOT NULL REFERENCES Contest(contest_id),
    presenter_id INT NOT NULL REFERENCES Presenter(presenter_id),
    notes TEXT,
    PRIMARY KEY (contest_id, presenter_id)
);