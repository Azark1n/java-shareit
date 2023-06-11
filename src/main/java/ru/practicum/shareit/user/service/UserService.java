package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import javax.validation.Valid;
import java.util.List;

public interface UserService {
    User create(@Valid User user);

    List<User> getAll();

    User getByIdOrThrow(int id);

    User update(@Valid User user);

    boolean deleteById(int id);
}
