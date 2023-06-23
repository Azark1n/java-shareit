package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class BookingDto {
    public interface Create {}

    @Null(groups = Create.class)
    Integer id;

    @NotNull(groups = Create.class)
    @FutureOrPresent(groups = Create.class)
    LocalDateTime start;

    @NotNull(groups = Create.class)
    @FutureOrPresent(groups = Create.class)
    LocalDateTime end;

    @NotNull(groups = Create.class)
    Integer itemId;

    @Null(groups = Create.class)
    Item item;

    @Null(groups = Create.class)
    User booker;

    @Null(groups = Create.class)
    BookingStatus status;
}
