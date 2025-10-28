package com.joker.apostas.dtos;

import jakarta.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CredentialsDto {
    
    @NotEmpty
    private String identifier;

    @NotEmpty
    private char[] password;

}