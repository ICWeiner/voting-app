package com.joker.apostas.dto;

import java.time.LocalDateTime;

public class CreateContestDto {
    public String title;
    public String description;
    public LocalDateTime startDateTime;
    public LocalDateTime endDateTime;
    public Integer prize;
}
