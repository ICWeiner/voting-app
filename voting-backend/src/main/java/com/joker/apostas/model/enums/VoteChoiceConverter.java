package com.joker.apostas.model.enums;

import com.joker.apostas.dto.VoteChoice;

import jakarta.persistence.AttributeConverter;

public class VoteChoiceConverter implements AttributeConverter<VoteChoice, String> {

    @Override
    public String convertToDatabaseColumn(VoteChoice attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public VoteChoice convertToEntityAttribute(String dbData) {
        return dbData == null ? null : VoteChoice.valueOf(dbData);
    }
}
