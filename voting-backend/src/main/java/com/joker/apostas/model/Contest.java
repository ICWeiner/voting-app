package com.joker.apostas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "contest")
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contestid")
    private Long id;

    @Column(name = "title", length = 250)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "startdatetime")
    private LocalDateTime startDateTime;

    @Column(name = "enddatetime")
    private LocalDateTime endDateTime;

    @Column(name = "prize")
    private Integer prize;

    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("contest-contestants")
    private List<ContestContestant> contestContestants;

    public Contest() {}

    public Contest(String title, String description, LocalDateTime startDateTime,
                   LocalDateTime endDateTime, Integer prize, Contestant contestant) {
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.prize = prize;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public LocalDateTime getEndDateTime() { return endDateTime; }
    public Integer getPrize() { return prize; }
    public List<ContestContestant> getContestContestants() {return contestContestants; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }
    public void setPrize(Integer prize) { this.prize = prize; }
    public void setContestContestants(List<ContestContestant> contestContestants) {this.contestContestants = contestContestants; }
}
