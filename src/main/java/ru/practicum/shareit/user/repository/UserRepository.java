package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User create(User user);

    List<User> getAll();

    Optional<User> getById(int id);

    Optional<User> getByEmail(String email);

    User update(User user);

    boolean deleteById(int id);
}
