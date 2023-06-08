package ru.practicum.shareit.item.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Validated
@Data
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;

    @Override
    public Item create(@Valid Item item) {
        if (userRepository.getById(item.getOwner().getId()).isEmpty()) {
            throw new NotFoundException(String.format("User with id %d not found", item.getOwner().getId()));
        }
        log.info(String.format("Create item: %s", item));

        return repository.create(item);
    }

    @Override
    public Item getByIdOrThrow(int id) {
        return repository.getById(id).orElseThrow(() -> new NotFoundException(String.format("Item with id %d not found", id)));
    }

    @Override
    public Item update(Item item) {
        log.info(String.format("Update item: %s", item.toString()));

        return repository.update(item);
    }

    @Override
    public List<Item> getAllByUser(User user) {
        return repository.getAllByUser(user);
    }

    @Override
    public List<Item> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        return repository.search(text);
    }
}
