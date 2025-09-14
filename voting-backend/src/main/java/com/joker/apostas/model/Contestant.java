package com.joker.apostas.model;

import java.util.List;

import com.joker.apostas.model.enums.StudiesType;

import jakarta.persistence.*;

@Entity
@Table(name = "contestant")
public class Contestant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contestantid")
    private Long id;

    @Column(name = "contestantname", length = 100)
    private String name;

    @Column(name = "profession", length = 100)
    private String profession;

    @Column(name = "age")
    private Integer age;

    @Column(name = "studies")
    @Enumerated(EnumType.STRING)
    private StudiesType studies;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "contestant", cascade = CascadeType.ALL)
    private List<ContestContestant> contestContestants;

    public Contestant() {}

    public Contestant(String name, String profession, Integer age, StudiesType studies, String notes) {
        this.name = name;
        this.profession = profession;
        this.age = age;
        this.studies = studies;
        this.notes = notes;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getProfession() { return profession; }
    public Integer getAge() { return age; }
    public StudiesType getStudies() { return studies; }
    public String getNotes() { return notes; }
    public List<ContestContestant> getContestContestants() { return contestContestants; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setProfession(String profession) { this.profession = profession; }
    public void setAge(Integer age) { this.age = age; }
    public void setStudies(StudiesType studies) { this.studies = studies; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setContestContestants(List<ContestContestant> contestContestants) { this.contestContestants = contestContestants; }
}
