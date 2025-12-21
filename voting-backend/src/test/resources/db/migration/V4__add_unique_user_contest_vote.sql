ALTER TABLE vote
    ADD CONSTRAINT unique_user_contest_vote
        UNIQUE (user_id, contest_id);