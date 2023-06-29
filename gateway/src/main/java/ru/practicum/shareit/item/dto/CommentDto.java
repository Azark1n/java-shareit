package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class CommentDto {
    @NotNull
    @NotBlank
    private String text;
}
