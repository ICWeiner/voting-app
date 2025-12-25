package com.joker.apostas.mapper;

import com.joker.apostas.dto.ContestStatsDto;
import com.joker.apostas.dto.VoteChoice;
import com.joker.apostas.dto.VoteRequestDto;
import com.joker.apostas.model.Contest;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ContestMapper {

    @Mapping(target = "contestId", source = "contest.id")
    @Mapping(target = "title", source = "contest.title")
    @Mapping(
            target = "status",
            source = "contest.status") // MapStruct handles Enum string mapping automatically
    @Mapping(target = "results", source = "resultsMap", qualifiedByName = "mapEnumKeysToStrings")
    @Mapping(target = "totalVotes", expression = "java(calculateTotal(resultsMap))")
    ContestStatsDto toStatsDto(Contest contest, Map<VoteChoice, Long> resultsMap);

    @Named("mapEnumKeysToStrings")
    default Map<String, Long> mapEnumKeysToStrings(Map<VoteChoice, Long> resultsMap) {
        if (resultsMap == null) return null;
        return resultsMap.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().name(), Map.Entry::getValue));
    }

    default Long calculateTotal(Map<VoteChoice, Long> resultsMap) {
        if (resultsMap == null) return 0L;
        return resultsMap.values().stream().mapToLong(Long::longValue).sum();
    }
}
