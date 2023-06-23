package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class BookingDto {
    public interface Create {}

    @Null(groups = Create.class)
    private Integer id;

    @NotNull(groups = Create.class)
    @FutureOrPresent(groups = Create.class)
    private LocalDateTime start;

    @NotNull(groups = Create.class)
    @FutureOrPresent(groups = Create.class)
    private LocalDateTime end;

    @NotNull(groups = Create.class)
    private Integer itemId;

    @Null(groups = Create.class)
    private Item item;

    @Null(groups = Create.class)
    private User booker;

    @Null(groups = Create.class)
    private BookingStatus status;
}
