package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class CommentDto {
    public interface Create {}

    @Null(groups = Create.class)
    Integer id;

    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    String text;

    @Null(groups = Create.class)
    String authorName;

    @Null(groups = Create.class)
    LocalDateTime created;
}
