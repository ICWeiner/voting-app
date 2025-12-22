package com.joker.apostas.model;

import com.joker.apostas.model.id.ContestPresenterId;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contestpresenter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContestPresenter {

    @EmbeddedId private ContestPresenterId id;

    @MapsId("contestId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    @JsonBackReference
    private Contest contest;

    @MapsId("presenterId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "presenter_id")
    @JsonBackReference
    private Presenter presenter;

    @Column(name = "notes")
    private String notes;

    public ContestPresenter(Contest contest, Presenter presenter, String notes) {
        this.contest = contest;
        this.presenter = presenter;
        this.notes = notes;
        this.id = new ContestPresenterId(contest.getId(), presenter.getId());
    }
}
