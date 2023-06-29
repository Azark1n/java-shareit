package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class UserDto {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    @Email
    private String email;
}
