package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserDto create(UserDto userDto);

    List<UserDto> getAll();

    UserDto getById(int id);

    UserDto patch(int id, Map<String, Object> patchValues);

    boolean deleteById(int id);
}
