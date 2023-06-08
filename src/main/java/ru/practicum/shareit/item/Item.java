package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder(toBuilder = true)
public class Item {
    int id;

    @NotNull
    @NotBlank
    String name;

    @NotNull
    @NotBlank
    String description;

    @NotNull
    Boolean available;

    @NotNull
    User owner;

    ItemRequest request;
}
