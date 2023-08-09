package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class UserDto {
    private Integer id;

    private String name;

    private String email;
}
