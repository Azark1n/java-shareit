package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class BookingShortDto {
    private Integer id;

    private Integer bookerId;
}
