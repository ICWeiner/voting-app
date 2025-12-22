package com.joker.apostas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "presenter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Presenter { // TODO: why do we need a presenter?

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "presenter_id")
    private Long id;

    @Column(name = "presenter_name", length = 100)
    private String name;

    @OneToMany(mappedBy = "presenter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContestPresenter> contestPresenters;
}
