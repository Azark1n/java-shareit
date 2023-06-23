package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
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
    List<Booking> findByBookerOrderByStartDesc(User booker, Pageable pageable);

    List<Booking> findByItem_OwnerOrderByStartDesc(User owner, Pageable pageable);

    List<Booking> findByBookerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
            User booker, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findByItem_OwnerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
            User owner, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findByBookerAndEndLessThanOrderByStartDesc(User booker, LocalDateTime end, Pageable pageable);

    List<Booking> findByItem_OwnerAndEndLessThanOrderByStartDesc(User booker, LocalDateTime now, Pageable pageable);

    List<Booking> findByBookerAndStartGreaterThanOrderByStartDesc(User booker, LocalDateTime start, Pageable pageable);

    List<Booking> findByItem_OwnerAndStartGreaterThanOrderByStartDesc(User booker, LocalDateTime now, Pageable pageable);

    List<Booking> findByBookerAndStatusOrderByStartDesc(User booker, BookingStatus status, Pageable pageable);

    List<Booking> findByItem_OwnerAndStatusOrderByStartDesc(User owner, BookingStatus status, Pageable pageable);

    Booking findFirstByItemAndStartAfterAndStatusOrderByStart(Item item, LocalDateTime now, BookingStatus status);

    Booking findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(Item item, LocalDateTime start, BookingStatus status);

    Optional<Booking> findFirstByItemAndBookerAndStatusAndStartBefore(Item item, User booker, BookingStatus status, LocalDateTime start);
}
