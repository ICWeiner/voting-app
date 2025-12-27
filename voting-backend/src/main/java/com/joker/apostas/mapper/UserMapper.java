package com.joker.apostas.mapper;

import com.joker.apostas.dto.SignUpDto;
import com.joker.apostas.dto.UserDto;
import com.joker.apostas.model.User;
import com.joker.apostas.model.enums.Role;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "token", ignore = true)
    @Mapping(target = "role", source = "user.role", qualifiedByName = "RoleToString")
    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

    @Named("RoleToString")
    default String RoleToString(Role role) {
        return role != null ? role.name() : "USER";
    }
}
