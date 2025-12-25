package com.joker.apostas.service;

import com.joker.apostas.dto.LoginDto;
import com.joker.apostas.dto.SignUpDto;
import com.joker.apostas.dto.UserDto;

public interface AuthService {
    UserDto login(LoginDto loginDto);

    UserDto register(SignUpDto signUpDto);
}
