package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class BookingDto {
    public interface Create {
    }

    @NotNull(groups = BookingDto.Create.class)
    @FutureOrPresent(groups = BookingDto.Create.class)
    LocalDateTime start;

    @NotNull(groups = BookingDto.Create.class)
    @FutureOrPresent(groups = BookingDto.Create.class)
    LocalDateTime end;

    @NotNull(groups = BookingDto.Create.class)
    Integer itemId;
}
