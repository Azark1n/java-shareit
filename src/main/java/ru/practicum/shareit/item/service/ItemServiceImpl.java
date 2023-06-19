package ru.practicum.shareit.item.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Validated
@Data
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public Item create(@Valid Item item) {
        if (userRepository.findById(item.getOwner().getId()).isEmpty()) {
            throw new NotFoundException(String.format("User with id %d not found", item.getOwner().getId()));
        }
        log.info(String.format("Create item: %s", item));

        return repository.save(item);
    }

    @Override
    public Optional<Item> getById(int id) {
        return repository.findById(id);
    }

    @Override
    public Item getByIdOrThrow(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id=%d not found", id)));
    }

    @Override
    public Item getByIdAvailableForBookingOrThrow(int id) {
        return repository.findByIdAndAvailable(id, true)
                .orElseThrow(() -> new BadRequestException(
                        String.format("Item with id=%d unavailable for booking", id)));
    }

    @Override
    public Item update(Item item) {
        log.info(String.format("Update item: %s", item.toString()));

        return repository.save(item);
    }

    @Override
    public List<Item> getAllByUser(User user) {
        return repository.findByOwnerOrderById(user);
    }

    @Override
    public List<Item> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        return repository.findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCase(text);
    }

    @Override
    public List<Comment> getComments(Item item) {
        return commentRepository.findByItemOrderByCreatedDesc(item);
    }

    @Override
    public Comment createComment(Comment comment) {
        log.info(String.format("Create comment %s", comment));

        return commentRepository.save(comment);
    }
}
