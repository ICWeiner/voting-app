package com.joker.apostas.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.joker.apostas.model.enums.StudiesType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contestant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "studies", columnDefinition = "studies_type")
    @Enumerated(EnumType.STRING)
    private StudiesType studies;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "contestant", cascade = CascadeType.ALL)
    @JsonBackReference("contestant-contests")
    private List<ContestContestant> contestContestants;
}
