package com.joker.apostas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contest")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contest_id")
    private Long id;

    @Column(name = "title", length = 250)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @Column(name = "prize")
    private Integer prize;

    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("contest-contestants")
    private List<ContestContestant> contestContestants;
}
