package com.joker.apostas.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.joker.apostas.model.enums.StudiesType;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "contestant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contestant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contestant_id")
    private Long id;

    @Column(name = "contestant_name", length = 100)
    private String name;

    @Column(name = "profession", length = 100)
    private String profession;

    @Column(name = "age")
    private Integer age;

    @Column(name = "studies", columnDefinition="studies_type")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private StudiesType studies;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "contestant", cascade = CascadeType.ALL)
    @JsonBackReference("contestant-contests")
    private List<ContestContestant> contestContestants;
}
