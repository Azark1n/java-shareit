package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class ItemDto {
    public interface Create {}

    @Null(groups = Create.class)
    private Integer id;

    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    private String name;

    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    private String description;

    @NotNull(groups = Create.class)
    private Boolean available;

    private Integer requestId;
}
