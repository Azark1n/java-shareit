package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class CommentDto {
    public interface Create {}

    @Null(groups = Create.class)
    private Integer id;

    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    private String text;

    @Null(groups = Create.class)
    private String authorName;

    @Null(groups = Create.class)
    private LocalDateTime created;
}
