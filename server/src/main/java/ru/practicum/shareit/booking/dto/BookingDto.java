package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class BookingDto {
    private Integer id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Integer itemId;

    private Item item;

    private User booker;

    private BookingStatus status;
}
