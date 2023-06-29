package ru.practicum.shareit.booking.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Validated
@Data
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto create(BookingDto dto, int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ForbiddenException(
                String.format("Can't create booking for user id %d", userId)));

        Item item = itemRepository.findById(dto.getItemId()).orElseThrow(() -> new NotFoundException(
                String.format("Item with id %d not found", dto.getItemId())));

        if (!item.getAvailable()) {
            throw new BadRequestException(String.format("Item with id %d unavailable for booking", dto.getItemId()));
        }

        if (item.getOwner().equals(user)) {
            throw new NotFoundException(String.format("Can't booking item id=%d for user id=%d", dto.getItemId(), userId));
        }

        Booking booking = bookingMapper.toModel(dto, user, item, BookingStatus.WAITING);
        log.info(String.format("Create booking: %s", booking));

        return bookingMapper.toDto(repository.save(booking));
    }

    @Override
    public BookingDto patch(int bookingId, int userId, boolean approved) {
        Booking booking = repository.findById(bookingId).orElseThrow(() -> new NotFoundException(String.format(
                "Booking with id %d not found", bookingId)));

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format(
                "User with id %d not found", userId)));

        if (!Objects.equals(booking.getItem().getOwner(), user)) {
            throw new NotFoundException(String.format("Not found booking for user id %d and booking id %d", userId, bookingId));
        }

        BookingStatus bookingStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        if (booking.getStatus() == bookingStatus) {
            throw new BadRequestException(String.format("For booking with id %d status already is %s", bookingId, bookingStatus));
        }

        Booking updatedBooking = booking.toBuilder().status(bookingStatus).build();
        log.info(String.format("Update booking: %s", updatedBooking));

        return bookingMapper.toDto(repository.save(updatedBooking));
    }

    @Override
    public BookingDto getById(int bookingId, int userId) {
        Booking booking = repository.findById(bookingId).orElseThrow(() -> new NotFoundException(String.format(
                "Booking with id %d not found", bookingId)));

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format(
                "User with id %d not found", userId)));

        if (!Objects.equals(booking.getBooker(), user) && !Objects.equals(booking.getItem().getOwner(), user)) {
            throw new NotFoundException(String.format("Can't get booking info for user id=%d and booking id=%d", userId, bookingId));
        }

        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> getAllByBookerAndState(int userId, String stateParam, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ForbiddenException(
                String.format("Can't get booking list for booker id %d", userId)));

        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new UnsupportedStatusException("Unknown state: " + stateParam));

        Pageable pageable = PageRequest.of(from / size, size);

        List<Booking> result;
        switch (state) {
            case ALL:
                result = repository.findByBookerOrderByStartDesc(user, pageable);
                break;
            case CURRENT:
                result = repository.findByBookerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(user, LocalDateTime.now(), LocalDateTime.now(), pageable);
                break;
            case PAST:
                result = repository.findByBookerAndEndLessThanOrderByStartDesc(user, LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                result = repository.findByBookerAndStartGreaterThanOrderByStartDesc(user, LocalDateTime.now(), pageable);
                break;
            default:
                result = repository.findByBookerAndStatusOrderByStartDesc(user, BookingStatus.valueOf(state.toString()), pageable);
        }

        return result.stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllByOwnerAndState(int userId, String stateParam, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ForbiddenException(
                String.format("Can't get booking list for booker id %d", userId)));

        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new UnsupportedStatusException("Unknown state: " + stateParam));

        Pageable pageable = PageRequest.of(from / size, size);

        List<Booking> result;
        switch (state) {
            case ALL:
                result = repository.findByItem_OwnerOrderByStartDesc(user, pageable);
                break;
            case CURRENT:
                result = repository.findByItem_OwnerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(user, LocalDateTime.now(), LocalDateTime.now(), pageable);
                break;
            case PAST:
                result = repository.findByItem_OwnerAndEndLessThanOrderByStartDesc(user, LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                result = repository.findByItem_OwnerAndStartGreaterThanOrderByStartDesc(user, LocalDateTime.now(), pageable);
                break;
            default:
                result = repository.findByItem_OwnerAndStatusOrderByStartDesc(user, BookingStatus.valueOf(state.toString()), pageable);
        }

        return result.stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }
}
