package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class UserDto {
    public interface Create {}

    @Null(groups = Create.class)
    private Integer id;

    @NotBlank(groups = Create.class)
    private String name;

    @NotBlank(groups = Create.class)
    @Email(groups = Create.class)
    private String email;
}
