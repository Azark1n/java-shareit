package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class Booking {
    int id;

    @NotNull
    LocalDateTime start;

    @NotNull
    LocalDateTime end;

    @NotNull
    Item item;

    @NotNull
    User booker;

    @NotNull
    BookingStatus status;
}
