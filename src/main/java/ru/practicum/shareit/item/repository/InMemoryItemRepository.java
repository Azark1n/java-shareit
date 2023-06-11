package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Integer, Item> map = new HashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(1);

    @Override
    public Item create(Item item) {
        Item newItem = item.toBuilder()
                .id(currentId.getAndIncrement())
                .build();

        map.put(newItem.getId(), newItem);

        return map.get(newItem.getId());
    }

    @Override
    public Optional<Item> getById(int id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Item> getAllByUser(User user) {
        return map.values().stream()
                .filter(item -> item.getOwner().equals(user))
                .collect(Collectors.toList());
    }

    @Override
    public Item update(Item item) {
        map.put(item.getId(), item);

        return map.get(item.getId());
    }

    @Override
    public List<Item> search(String text) {
        return map.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }
}
