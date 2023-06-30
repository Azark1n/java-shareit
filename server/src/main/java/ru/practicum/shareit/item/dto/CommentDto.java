package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class CommentDto {
    private Integer id;

    private String text;

    private String authorName;

    private LocalDateTime created;
}
