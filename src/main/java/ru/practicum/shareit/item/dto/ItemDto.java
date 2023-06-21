package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class ItemDto {
    public interface Create {}

    @Null(groups = Create.class)
    Integer id;

    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    String name;

    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    String description;

    @NotNull(groups = Create.class)
    Boolean available;

    BookingShortDto lastBooking;

    BookingShortDto nextBooking;

    List<CommentDto> comments;
}
