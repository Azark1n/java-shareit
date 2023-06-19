package ru.practicum.shareit.booking.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Validated
@Data
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;

    @Override
    public Booking create(Booking booking) {
        log.info(String.format("Create booking: %s", booking));

        return repository.save(booking);
    }

    @Override
    public Optional<Booking> getById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<Booking> getAllByBooker(User user) {
        return repository.findByBookerOrderByStartDesc(user, Sort.by(Sort.Direction.DESC, "start"));
    }

    @Override
    public List<Booking> getAllByOwner(User owner) {
        return repository.findByItem_OwnerOrderByStartDesc(owner, Sort.by(Sort.Direction.DESC, "start"));
    }

    @Override
    public List<Booking> getCurrentByBooker(User booker, LocalDateTime now) {
        return repository.findByBookerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(booker, now, now,
                Sort.by(Sort.Direction.DESC, "start"));
    }

    @Override
    public List<Booking> getCurrentByOwner(User owner, LocalDateTime now) {
        return repository.findByItem_OwnerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(owner, now, now,
                Sort.by(Sort.Direction.DESC, "start"));
    }

    @Override
    public List<Booking> getPastByBooker(User booker, LocalDateTime now) {
        return repository.findByBookerAndEndLessThanOrderByStartDesc(booker, now,
                Sort.by(Sort.Direction.DESC, "start"));
    }

    @Override
    public List<Booking> getPastByOwner(User booker, LocalDateTime now) {
        return repository.findByItem_OwnerAndEndLessThanOrderByStartDesc(booker, now,
                Sort.by(Sort.Direction.DESC, "start"));
    }

    @Override
    public List<Booking> getFutureByBooker(User booker, LocalDateTime now) {
        return repository.findByBookerAndStartGreaterThanOrderByStartDesc(booker, now,
                Sort.by(Sort.Direction.DESC, "start"));
    }

    @Override
    public List<Booking> getFutureByOwner(User booker, LocalDateTime now) {
        return repository.findByItem_OwnerAndStartGreaterThanOrderByStartDesc(booker, now,
                Sort.by(Sort.Direction.DESC, "start"));
    }

    @Override
    public List<Booking> getAllByStateAndBooker(User booker, BookingStatus status) {
        return repository.findByBookerAndStatusOrderByStartDesc(booker, status);
    }

    @Override
    public List<Booking> getAllByStateAndOwner(User owner, BookingStatus status) {
        return repository.findByItem_OwnerAndStatusOrderByStartDesc(owner, status);
    }

    @Override
    public Booking getLastBooking(Item item, User user) {
        if (Objects.equals(item.getOwner(), user)) {
            return repository.findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(item,
                    LocalDateTime.now(), BookingStatus.APPROVED);
        } else {
            return null;
        }
    }

    @Override
    public Booking getNextBooking(Item item, User user) {
        if (Objects.equals(item.getOwner(), user)) {
            return repository.findFirstByItemAndStartAfterAndStatusOrderByStart(item, LocalDateTime.now(),
                    BookingStatus.APPROVED);
        } else {
            return null;
        }
    }

    @Override
    public Booking update(Booking item) {
        return repository.save(item);
    }
}
