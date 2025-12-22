package com.joker.apostas.mapper;

import com.joker.apostas.dto.SignUpDto;
import com.joker.apostas.dto.UserDto;
import com.joker.apostas.model.User;
import com.joker.apostas.model.enums.UserType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role", qualifiedByName = "userTypeToString")
    @Mapping(target = "token", ignore = true)
    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

    @Named("userTypeToString")
    default String userTypeToString(UserType userType) {
        return userType != null ? userType.toString() : "REGULAR";
    }
}
