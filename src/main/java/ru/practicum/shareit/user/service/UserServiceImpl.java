package ru.practicum.shareit.user.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Validated
@Data
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public User create(@Valid User user) {
        if (repository.getByEmail(user.getEmail()).isPresent()) {
            throw new AlreadyExistException(String.format("User with email %s already exist", user.getEmail()));
        }
        log.info(String.format("Create user: %s", user));

        return repository.create(user);
    }

    @Override
    public List<User> getAll() {
        return repository.getAll();
    }

    @Override
    public User getByIdOrThrow(int id) {
        return repository.getById(id).orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", id)));
    }

    @Override
    public User update(@Valid User user) {
        if (repository.getById(user.getId()).isEmpty()) {
            throw new NotFoundException(String.format("User with id %d not found", user.getId()));
        }
        Optional<User> byEmail = repository.getByEmail(user.getEmail());
        if (byEmail.isPresent() && byEmail.get().getId() != user.getId()) {
            throw new AlreadyExistException(String.format("User with email %s already exist", user.getEmail()));
        }
        log.info(String.format("Update user: %s", user));

        return repository.update(user);
    }

    @Override
    public boolean deleteById(int id) {
        return repository.deleteById(id);
    }
}
