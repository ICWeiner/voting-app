package com.joker.apostas.model;

import com.joker.apostas.model.enums.VoteChoice;
import com.joker.apostas.model.enums.VoteChoiceConverter;

import jakarta.persistence.*;

import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "vote",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "unique_user_contest_vote",
                    columnNames = {"user_id", "contest_id"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contest_id")
    private Contest contest;
    
    @Convert(converter = VoteChoiceConverter.class)
    @Column(name = "vote_choice", nullable = false, columnDefinition = "vote_choice")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private VoteChoice voteChoice;

    @Column(name = "vote_date_time", insertable = false, updatable = false)
    private LocalDateTime date;
}
