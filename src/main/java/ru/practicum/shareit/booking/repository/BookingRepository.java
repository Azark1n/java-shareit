package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {
    List<Booking> findByBookerOrderByStartDesc(User booker, Sort sort);

    List<Booking> findByItem_OwnerOrderByStartDesc(User owner, Sort sort);

    List<Booking> findByBookerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
            User booker, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findByItem_OwnerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(
            User owner, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findByBookerAndEndLessThanOrderByStartDesc(User booker, LocalDateTime end, Sort sort);

    List<Booking> findByItem_OwnerAndEndLessThanOrderByStartDesc(User booker, LocalDateTime now, Sort sort);

    List<Booking> findByBookerAndStartGreaterThanOrderByStartDesc(User booker, LocalDateTime start, Sort sort);

    List<Booking> findByItem_OwnerAndStartGreaterThanOrderByStartDesc(User booker, LocalDateTime now, Sort sort);

    List<Booking> findByBookerAndStatusOrderByStartDesc(User booker, BookingStatus status);

    List<Booking> findByItem_OwnerAndStatusOrderByStartDesc(User owner, BookingStatus status);

    Booking findFirstByItemAndStartAfterAndStatusOrderByStart(Item item, LocalDateTime now, BookingStatus status);

    Booking findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(Item item, LocalDateTime start, BookingStatus status);
}
