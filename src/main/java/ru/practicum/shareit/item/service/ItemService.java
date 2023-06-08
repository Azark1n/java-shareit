package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.Valid;
import java.util.List;

public interface ItemService {

    Item create(@Valid Item item);

    Item getByIdOrThrow(int id);

    List<Item> getAllByUser(User user);

    Item update(Item item);

    List<Item> search(String text);
}
