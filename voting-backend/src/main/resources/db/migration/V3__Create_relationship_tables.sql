CREATE TABLE Vote (
    vote_id BIGSERIAL PRIMARY KEY,
    vote_choice vote_choice NOT NULL,
    vote_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL REFERENCES Users(user_id),
    contest_id BIGINT NOT NULL REFERENCES Contest(contest_id)
);

CREATE TABLE ContestContestant (
    is_super_joker BOOLEAN NOT NULL,
    contest_id BIGINT NOT NULL REFERENCES Contest(contest_id),
    contestant_id BIGINT NOT NULL REFERENCES Contestant(contestant_id),
    PRIMARY KEY (contest_id, contestant_id)
);

CREATE TABLE ContestPresenter (
    notes TEXT,
    contest_id BIGINT NOT NULL REFERENCES Contest(contest_id),
    presenter_id BIGINT NOT NULL REFERENCES Presenter(presenter_id),
    PRIMARY KEY (contest_id, presenter_id)
);