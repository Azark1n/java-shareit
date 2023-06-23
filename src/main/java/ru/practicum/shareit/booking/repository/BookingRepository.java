package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {
    List<Booking> findByBookerOrderByStartDesc(User booker);

    List<Booking> findByItem_OwnerOrderByStartDesc(User owner);

    List<Booking> findByBookerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
            User booker, LocalDateTime start, LocalDateTime end);

    List<Booking> findByItem_OwnerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
            User owner, LocalDateTime start, LocalDateTime end);

    List<Booking> findByBookerAndEndLessThanOrderByStartDesc(User booker, LocalDateTime end);

    List<Booking> findByItem_OwnerAndEndLessThanOrderByStartDesc(User booker, LocalDateTime now);

    List<Booking> findByBookerAndStartGreaterThanOrderByStartDesc(User booker, LocalDateTime start);

    List<Booking> findByItem_OwnerAndStartGreaterThanOrderByStartDesc(User booker, LocalDateTime now);

    List<Booking> findByBookerAndStatusOrderByStartDesc(User booker, BookingStatus status);

    List<Booking> findByItem_OwnerAndStatusOrderByStartDesc(User owner, BookingStatus status);

    Booking findFirstByItemAndStartAfterAndStatusOrderByStart(Item item, LocalDateTime now, BookingStatus status);

    Booking findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(Item item, LocalDateTime start, BookingStatus status);

    Optional<Booking> findFirstByItemAndBookerAndStatusAndStartBefore(Item item, User booker, BookingStatus status, LocalDateTime start);
}
