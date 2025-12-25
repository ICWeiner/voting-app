package com.joker.apostas.model;

import com.joker.apostas.dto.ContestStatus;
import com.joker.apostas.dto.VoteChoice;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContestStats {
    private Long contestId;
    private String title;
    private ContestStatus status;
    private Long totalVotes;
    private Map<VoteChoice, Long> results;
}
