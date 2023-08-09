package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class ItemDto {
    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    private Integer requestId;
}
