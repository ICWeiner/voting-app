package com.joker.apostas.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.joker.apostas.model.id.ContestContestantId;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contestcontestant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContestContestant {

    @EmbeddedId private ContestContestantId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("contestId")
    @JoinColumn(name = "contest_id")
    @JsonBackReference("contest-contestants") // TODO: why is this here?
    private Contest contest;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("contestantId")
    @JoinColumn(name = "contestant_id")
    @JsonManagedReference("contestant-contests") // TODO: why is this here?
    private Contestant contestant;

    @Column(name = "is_super_joker", nullable = false)
    private Boolean isSuperJoker;

    public ContestContestant(Contest contest, Contestant contestant, Boolean isSuperJoker) {
        this.contest = contest;
        this.contestant = contestant;
        this.isSuperJoker = isSuperJoker;
        this.id = new ContestContestantId(contest.getId(), contestant.getId());
    }
}
