package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
@Builder(toBuilder = true)
public class UserDto {
    public interface Create {}

    @NotBlank(groups = Create.class)
    String name;

    @NotBlank(groups = Create.class)
    @Email(groups = Create.class)
    String email;
}
