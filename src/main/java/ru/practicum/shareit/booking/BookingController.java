package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Value
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    BookingService service;
    UserService userService;
    ItemService itemService;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public Booking create(@RequestHeader(USER_ID_HEADER) @NotNull int userId,
                          @Validated(BookingDto.Create.class) @RequestBody BookingDto bookingDto) {
        User owner = userService.getById(userId).orElseThrow(() -> new ForbiddenException(
                String.format("Can't create booking for user id=%d", userId)));
        Item item = itemService.getById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Item with id=%d not found", bookingDto.getItemId())));
        if (!item.getAvailable()) {
            throw new BadRequestException(String.format("Item with id=%d unavailable for booking", bookingDto
                    .getItemId()));
        }
        if (!bookingDto.getStart().isBefore(bookingDto.getEnd())) {
            throw new BadRequestException("Can't create booking where start is not before end");
        }

        return service.create(BookingMapper.toModel(bookingDto, owner, item, BookingStatus.WAITING));
    }

    @PatchMapping("/{bookingId}")
    public Booking patch(@RequestHeader(USER_ID_HEADER) @NotNull int userId,
                         @PathVariable int bookingId, @RequestParam @NotNull boolean approved) {
        Booking booking = service.getById(bookingId).orElseThrow(() -> new NotFoundException(String.format(
                "Booking with id=%d not found", bookingId)));
        User owner = userService.getById(userId).orElseThrow(() -> new NotFoundException(String.format(
                "User with id=%d not found", userId)));
        if (!Objects.equals(booking.item.getOwner(), owner)) {
            throw new NotFoundException(String.format("Not found booking for user id=%d and booking id=%d",
                    userId, bookingId));
        }
        BookingStatus bookingStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        return service.update(booking.toBuilder().status(bookingStatus).build());
    }

    @GetMapping("/{bookingId}")
    public Booking get(@RequestHeader(USER_ID_HEADER) @NotNull int userId, @PathVariable int bookingId) {
        Booking booking = service.getById(bookingId).orElseThrow(() -> new NotFoundException(String.format(
                "Booking with id=%d not found", bookingId)));
        Optional<User> owner = userService.getById(userId);
        if (owner.isEmpty() || (!Objects.equals(booking.booker, owner.get())
                && !Objects.equals(booking.item.getOwner(), owner.get()))) {
            throw new NotFoundException(String.format("Can't get booking info for user id=%d and booking id=%d",
                    userId, bookingId));
        }
        return booking;
    }

    @GetMapping
    public List<Booking> getAllByBooker(@RequestHeader(USER_ID_HEADER) @NotNull int userId,
                                        @RequestParam(defaultValue = "all") String state) {
        User booker = userService.getById(userId).orElseThrow(() -> new ForbiddenException(
                String.format("Can't get booking list for booker id=%d", userId)));
        state = state.toLowerCase();
        switch (state) {
            case "all":
                return service.getAllByBooker(booker);
            case "current":
                return service.getCurrentByBooker(booker, LocalDateTime.now());
            case "past":
                return service.getPastByBooker(booker, LocalDateTime.now());
            case "future":
                return service.getFutureByBooker(booker, LocalDateTime.now());
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @GetMapping("/owner")
    public List<Booking> getAllByOwner(@RequestHeader(USER_ID_HEADER) @NotNull int userId,
                                        @RequestParam(defaultValue = "all") String state) {
        User owner = userService.getById(userId).orElseThrow(() -> new ForbiddenException(
                String.format("Can't get booking list for owner id=%d", userId)));
        state = state.toLowerCase();
        switch (state) {
            case "all":
                return service.getAllByOwner(owner);
            case "current":
                return service.getCurrentByOwner(owner, LocalDateTime.now());
            case "past":
                return service.getPastByOwner(owner, LocalDateTime.now());
            case "future":
                return service.getFutureByOwner(owner, LocalDateTime.now());
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
