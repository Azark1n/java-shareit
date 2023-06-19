package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface ItemService {

    Item create(@Valid Item item);

    Optional<Item> getById(int id);

    Item getByIdOrThrow(int id);

    List<Item> getAllByUser(User user);

    Item getByIdAvailableForBookingOrThrow(int id);

    Item update(Item item);

    List<Item> search(String text);

    List<Comment> getComments(Item item);

    Comment createComment(Comment comment);
}
