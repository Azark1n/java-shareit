package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class ItemRequest {
    int id;

    @NonNull
    @NotBlank
    String descriprtion;

    @NotNull
    User requestor;

    @NotNull
    LocalDateTime created;
}
