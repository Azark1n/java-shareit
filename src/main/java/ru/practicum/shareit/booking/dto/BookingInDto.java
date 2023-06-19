package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class BookingInDto {
    public interface Create {
    }

    @NotNull(groups = BookingInDto.Create.class)
    @FutureOrPresent(groups = BookingInDto.Create.class)
    LocalDateTime start;

    @NotNull(groups = BookingInDto.Create.class)
    @FutureOrPresent(groups = BookingInDto.Create.class)
    LocalDateTime end;

    @NotNull(groups = BookingInDto.Create.class)
    Integer itemId;
}
