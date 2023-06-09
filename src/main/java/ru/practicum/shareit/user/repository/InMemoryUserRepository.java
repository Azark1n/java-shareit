package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryUserRepository implements UserRepository {
    private final Map<Integer, User> map = new HashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(1);

    @Override
    public User create(User user) {
        User newUser = user.toBuilder()
                .id(currentId.getAndIncrement())
                .build();

        map.put(newUser.getId(), newUser);

        return map.get(newUser.getId());
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public Optional<User> getById(int id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return map.values().stream()
                .filter(user -> Objects.equals(user.getEmail(), email))
                .findFirst();
    }

    @Override
    public User update(User user) {
        map.put(user.getId(), user);

        return map.get(user.getId());
    }

    @Override
    public boolean deleteById(int id) {
        if (map.containsKey(id)) {
            map.remove(id);
            return true;
        } else {
            return false;
        }
    }
}
