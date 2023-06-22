package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class ItemRequestDto {
    public interface Create {}

    @Null(groups = Create.class)
    Integer id;

    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    String description;

    @Null(groups = Create.class)
    LocalDateTime created;

    @Null(groups = Create.class)
    List<ItemDto> items;
}
