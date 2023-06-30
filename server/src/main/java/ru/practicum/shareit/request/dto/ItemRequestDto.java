package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class ItemRequestDto {
    private Integer id;

    private String description;

    private LocalDateTime created;
}
