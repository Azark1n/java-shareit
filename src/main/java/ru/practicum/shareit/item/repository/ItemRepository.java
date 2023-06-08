package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item create(Item item);

    Optional<Item> getById(int id);

    List<Item> getAllByUser(User user);

    Item update(Item item);

    List<Item> search(String text);
}
