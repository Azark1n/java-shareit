package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.booking.dto.BookingShortOutDto;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class ItemOutDto {
    int id;

    String name;

    String description;

    Boolean available;

    BookingShortOutDto lastBooking;

    BookingShortOutDto nextBooking;

    List<CommentOutDto> comments;
}
