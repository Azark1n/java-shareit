package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class ItemInDto {
    public interface Create {}

    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    String name;

    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    String description;

    @NotNull(groups = Create.class)
    Boolean available;
}
