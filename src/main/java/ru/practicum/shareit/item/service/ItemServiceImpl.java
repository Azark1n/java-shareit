package ru.practicum.shareit.item.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Validated
@Data
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final Validator validator;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemDto create(ItemDto dto, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        Item item = itemMapper.toModel(dto, user);
        if (dto.getRequestId() != null && dto.getRequestId() > 0) {
            ItemRequest itemRequest = itemRequestRepository.findById(dto.getRequestId())
                    .orElseThrow(() -> new NotFoundException(String.format("Item request with id %d not found", dto.getRequestId())));
            item.setRequest(itemRequest);
        }

        log.info(String.format("Create item: %s", item));

        return itemMapper.toDto(repository.save(item));
    }

    @Override
    public ItemDto patch(int id, int userId, Map<String, Object> patchValues) {
        Item existItem = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %d not found", id)));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        if (!existItem.getOwner().equals(user)) {
            throw new ForbiddenException(String.format("Patch item with id %d for user with id %d forbidden", id, userId));
        }

        ItemDto dto = itemMapper.toDto(existItem);

        for (Map.Entry<String, Object> entry : patchValues.entrySet()) {
            try {
                BeanUtils.copyProperty(dto, entry.getKey(), entry.getValue());
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new BadRequestException(e.getMessage());
            }
        }

        Errors errors = new BeanPropertyBindingResult(dto, UserDto.class.getName());
        validator.validate(dto, errors);

        log.info(String.format("Update item: %s", dto));

        Item item = itemMapper.toModel(dto, user);
        if (dto.getRequestId() != null && dto.getRequestId() > 0) {
            ItemRequest itemRequest = itemRequestRepository.findById(dto.getRequestId())
                    .orElseThrow(() -> new NotFoundException(String.format("Item request with id %d not found", dto.getRequestId())));
            item.setRequest(itemRequest);
        }

        return itemMapper.toDto(repository.save(item));
    }

    @Override
    public ItemExtraDto getById(int id, int userId) {
        Item item = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %d not found", id)));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        List<CommentDto> comments = commentRepository.findByItemOrderByCreatedDesc(item).stream().map(commentMapper::toDto).collect(Collectors.toList());
        return itemMapper.toExtraDto(item, getLastBookingDto(item, user), getNextBookingDto(item, user), comments);
    }

    private BookingShortDto getLastBookingDto(Item item, User user) {
        if (Objects.equals(item.getOwner(), user)) {
            return bookingMapper.toShortDto(bookingRepository.findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(item,
                    LocalDateTime.now(), BookingStatus.APPROVED));
        }
        return null;
    }

    private BookingShortDto getNextBookingDto(Item item, User user) {
        if (Objects.equals(item.getOwner(), user)) {
            return bookingMapper.toShortDto(bookingRepository.findFirstByItemAndStartAfterAndStatusOrderByStart(item, LocalDateTime.now(),
                    BookingStatus.APPROVED));
        }
        return null;
    }

    @Override
    public List<ItemExtraDto> getAllByUserId(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        return repository.findByOwnerOrderById(user).stream()
                .map(item -> {
                            List<CommentDto> comments = commentRepository.findByItemOrderByCreatedDesc(item).stream().map(commentMapper::toDto).collect(Collectors.toList());
                            return itemMapper.toExtraDto(item, getLastBookingDto(item, user), getNextBookingDto(item, user), comments);
                        }
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        if (text.isBlank()) {
            return new ArrayList<>();
        }

        return repository.findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCase(text).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(CommentDto dto, int id, int userId) {
        Item item = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %d not found", id)));

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        bookingRepository.findFirstByItemAndBookerAndStatusAndStartBefore(item, booker, BookingStatus.APPROVED, LocalDateTime.now())
                .orElseThrow(() -> new BadRequestException(String.format("Can't create comment for item id %d and user id %d", id, userId)));

        Comment comment = commentMapper.toModel(dto, booker, item);
        log.info(String.format("Create comment %s", comment));

        return commentMapper.toDto(commentRepository.save(comment));
    }
}
