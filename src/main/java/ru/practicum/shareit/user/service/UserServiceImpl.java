package ru.practicum.shareit.user.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
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
        log.info(String.format("Create user: %s", user));

        return repository.save(user);
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public User getByIdOrThrow(int id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", id)));
    }

    @Override
    public Optional<User> getById(int id) {
        return repository.findById(id);
    }

    @Override
    public User update(@Valid User user) {
        if (repository.findById(user.getId()).isEmpty()) {
            throw new NotFoundException(String.format("User with id %d not found", user.getId()));
        }
        log.info(String.format("Update user: %s", user));

        return repository.save(user);
    }

    @Override
    public boolean deleteById(int id) {
        boolean exist = repository.findById(id).isPresent();
        repository.deleteById(id);

        return exist;
    }
}
