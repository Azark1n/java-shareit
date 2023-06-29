package ru.practicum.shareit.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.user.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDto toDto(User model);

    User toModel(UserDto dto);
}
