package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class ItemExtraDto {
    public interface Create {}

    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    private Integer requestId;

    private BookingShortDto lastBooking;

    private BookingShortDto nextBooking;

    private List<CommentDto> comments;
}
