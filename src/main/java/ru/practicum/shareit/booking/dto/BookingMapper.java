package ru.practicum.shareit.booking.dto;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@Slf4j
public class BookingMapper {
    private BookingMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Booking toModel(BookingInDto bookingInDto, User owner, Item item, BookingStatus status) {
        return Booking.builder()
                .start(bookingInDto.getStart())
                .end(bookingInDto.getEnd())
                .item(item)
                .booker(owner)
                .status(status)
                .build();
    }

    public static BookingShortOutDto toShortDto(Booking booking) {
        return BookingShortOutDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}