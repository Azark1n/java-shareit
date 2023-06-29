package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class ItemRequestExtraDto {
    public interface Create {}

    private Integer id;

    private String description;

    private LocalDateTime created;

    private List<ItemDto> items;
}
