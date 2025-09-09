package com.joker.apostas.model.id;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ContestContestantId implements Serializable {

    private Long contestId;
    private Long contestantId;

    public ContestContestantId() {}
    
    public ContestContestantId(Long contestId, Long contestantId) {
        this.contestId = contestId;
        this.contestantId = contestantId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        ContestContestantId that = (ContestContestantId) o;
        return Objects.equals(contestId, that.contestId) &&
               Objects.equals(contestantId, that.contestantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contestId, contestantId);
    }
}
