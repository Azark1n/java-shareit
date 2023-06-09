package ru.practicum.shareit.request.dto;

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
public class ItemRequestDto {
    public interface Create {}

    @Null(groups = Create.class)
    private Integer id;

    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    private String description;

    @Null(groups = Create.class)
    private LocalDateTime created;
}
