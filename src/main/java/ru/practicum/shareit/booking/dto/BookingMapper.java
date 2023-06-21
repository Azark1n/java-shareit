package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@UtilityClass
public class BookingMapper {
    public static Booking toModel(BookingDto dto, User owner, Item item, BookingStatus status) {
        return Booking.builder()
                .start(dto.getStart())
                .end(dto.getEnd())
                .item(item)
                .booker(owner)
                .status(status)
                .build();
    }

    public static BookingDto toDto(Booking model) {
        return BookingDto.builder()
                .id(model.getId())
                .start(model.getStart())
                .end(model.getEnd())
                .itemId(model.getItem().getId())
                .item(model.getItem())
                .booker(model.getBooker())
                .status(model.getStatus())
                .build();
    }

    public static BookingShortDto toShortDto(Booking model) {
        return model == null ? null : BookingShortDto.builder()
                .id(model.getId())
                .bookerId(model.getBooker().getId())
                .build();
    }
}