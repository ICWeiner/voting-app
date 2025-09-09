package com.joker.apostas.model;
import com.joker.apostas.model.id.ContestContestantId;

import jakarta.persistence.*;

@Entity
@Table(name = "contestcontestant")
public class ContestContestant {

    @EmbeddedId
    private ContestContestantId id;

    @ManyToOne
    @MapsId("contestId")
    @JoinColumn(name = "contestid")
    private Contest contest;

    @ManyToOne
    @MapsId("contestantId")
    @JoinColumn(name = "contestantid")
    private Contestant contestant;

    @Column(name = "issuperjoker", nullable = false)
    private Boolean isSuperJoker;

    public ContestContestant() {}

    public ContestContestant(Contest contest, Contestant contestant, Boolean isSuperJoker) {
        this.contest = contest;
        this.contestant = contestant;
        this.isSuperJoker = isSuperJoker;
    }

    // Getters & Setters
    public ContestContestantId getId() { return id; }
    public Contest getContest() { return contest; }
    public Contestant getContestant() { return contestant; }
    public Boolean getIsSuperJoker() { return isSuperJoker; }

    public void setId(ContestContestantId id) { this.id = id; }
    public void setContest(Contest contest) { this.contest = contest; }
    public void setContestant(Contestant contestant) { this.contestant = contestant; }
    public void setIsSuperJoker(Boolean isSuperJoker) { this.isSuperJoker = isSuperJoker; }
}
