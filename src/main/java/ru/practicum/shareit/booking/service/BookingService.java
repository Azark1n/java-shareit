package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface BookingService {
    Booking create(@Valid Booking booking);

    Optional<Booking> getById(int id);

    List<Booking> getAllByBooker(User user);

    List<Booking> getAllByOwner(User owner);

    List<Booking> getCurrentByBooker(User booker, LocalDateTime now);

    List<Booking> getCurrentByOwner(User owner, LocalDateTime now);

    List<Booking> getPastByBooker(User booker, LocalDateTime now);

    List<Booking> getPastByOwner(User booker, LocalDateTime now);

    List<Booking> getFutureByBooker(User booker, LocalDateTime now);

    List<Booking> getFutureByOwner(User booker, LocalDateTime now);

    List<Booking> getAllByStateAndBooker(User booker, BookingStatus status);

    List<Booking> getAllByStateAndOwner(User owner, BookingStatus status);

    Booking getLastBooking(Item item, User user);

    Booking getNextBooking(Item item, User user);

    Booking update(Booking item);
}
