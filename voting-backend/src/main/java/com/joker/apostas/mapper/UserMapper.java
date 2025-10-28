package com.joker.apostas.mapper;

import com.joker.apostas.dtos.SignUpDto;
import com.joker.apostas.dtos.UserDto;
import com.joker.apostas.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

}