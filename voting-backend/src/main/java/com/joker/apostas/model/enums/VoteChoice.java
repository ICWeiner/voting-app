package com.joker.apostas.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum VoteChoice {
    V0(0),
    V200(200),
    V500(500),
    V1000(1000),
    V3000(3000),
    V10000(10000),
    V50000(50000);

    private final int value;

    VoteChoice(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static VoteChoice fromValue(int value) {
        return Arrays.stream(values())
                .filter(v -> v.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid vote choice: " + value));
    }
}
