package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Service
public interface BookingService {
    BookingDto create(BookingDto dto, int userId);

    BookingDto patch(int bookingId, int userId, boolean approved);

    BookingDto getById(int bookingId, int userId);

    List<BookingDto> getAllByBookerAndState(int userId, String state);

    List<BookingDto> getAllByOwnerAndState(int userId, String state);
}
