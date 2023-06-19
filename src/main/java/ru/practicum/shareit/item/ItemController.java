package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Value
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    ItemService service;
    UserService userService;
    BookingService bookingService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public Item create(@RequestHeader(USER_ID_HEADER) @NotNull int userId, @Validated(ItemInDto.Create.class) @RequestBody ItemInDto itemInDto) {
        User owner = userService.getByIdOrThrow(userId);

        return service.create(ItemMapper.toModel(owner, itemInDto));
    }

    @PatchMapping("/{id}")
    public Item patch(@RequestHeader(USER_ID_HEADER) @NotNull int userId, @PathVariable int id, @Validated(ItemInDto.Create.class) @RequestBody Map<String, Object> patchValues) {
        User owner = userService.getByIdOrThrow(userId);
        Item item = service.getByIdOrThrow(id);

        if (!item.getOwner().equals(owner)) {
            throw new ForbiddenException(String.format("Patch item with id=%d for user with id=%d forbidden", id, userId));
        }

        Item patchedItem = ItemMapper.patch(item, patchValues);

        return service.update(patchedItem);
    }

    @GetMapping("/{id}")
    public ItemOutDto getById(@RequestHeader(USER_ID_HEADER) @NotNull int userId, @PathVariable int id) {
        User owner = userService.getById(userId).orElseThrow(() -> new NotFoundException(String.format(
                "User with id=%d not found", userId)));
        Item item = service.getById(id).orElseThrow(() -> new NotFoundException(String.format(
                "Item with id=%d not found", id)));
        Booking lastBooking = bookingService.getLastBooking(item, owner);
        Booking nextBooking = bookingService.getNextBooking(item, owner);
        List<Comment> comments = service.getComments(item);

        return ItemMapper.toDto(item, lastBooking, nextBooking, comments);
    }

    @PostMapping("/{id}/comment")
    public CommentOutDto createComment(@RequestHeader(USER_ID_HEADER) @NotNull int userId, @PathVariable int id,
                                       @RequestBody @Valid CommentInDto commentInDto) {
        User booker = userService.getById(userId).orElseThrow(() -> new NotFoundException(String.format(
                "User with id=%d not found", userId)));
        Optional<Booking> optionalBooking = bookingService.getById(id);
        if (optionalBooking.isEmpty() || !Objects.equals(optionalBooking.get().getBooker(), booker)
                || !BookingStatus.APPROVED.equals(optionalBooking.get().getStatus())) {
            throw new BadRequestException(String.format("Can't create comment for booking id=%d and user id=%d",
                    id, userId));
        }
        Comment comment = service.createComment(CommentMapper.toModel(commentInDto, booker, optionalBooking.get().getItem()));

        return CommentMapper.toDto(comment);
    }

    @GetMapping
    public List<ItemOutDto> getAllByUser(@RequestHeader(USER_ID_HEADER) @NotNull int userId) {
        User owner = userService.getByIdOrThrow(userId);

        List<Item> items = service.getAllByUser(owner);

        return items.stream()
                .map(item -> {
                    List<Comment> comments = service.getComments(item);
                    return ItemMapper.toDto(item, bookingService.getLastBooking(item, owner),
                            bookingService.getNextBooking(item, owner), comments);
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<Item> search(@RequestHeader(USER_ID_HEADER) @NotNull int userId, @RequestParam(value = "text") String searchText) {
        userService.getByIdOrThrow(userId);

        return service.search(searchText);
    }

}
