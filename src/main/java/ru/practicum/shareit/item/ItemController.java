package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Value
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    ItemService service;
    UserService userService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public Item create(@RequestHeader(USER_ID_HEADER) @NotNull int userId, @Valid @RequestBody ItemDto itemDto) {
        User owner = userService.getByIdOrThrow(userId);

        return service.create(ItemMapper.toModel(owner, itemDto));
    }

    @PatchMapping("/{id}")
    public Item patch(@RequestHeader(USER_ID_HEADER) @NotNull int userId, @PathVariable int id, @Validated(ItemDto.Create.class) @RequestBody Map<String,Object> patchValues) {
        User owner = userService.getByIdOrThrow(userId);
        Item item = service.getByIdOrThrow(id);

        if (!item.getOwner().equals(owner)) {
            throw new ForbiddenException(String.format("Patch item with id=%d for user with id=%d forbidden", id, userId));
        }

        Item patchedItem = ItemMapper.patch(item, patchValues);

        return service.update(patchedItem);
    }

    @GetMapping("/{id}")
    public Item getById(@RequestHeader(USER_ID_HEADER) @NotNull int userId, @PathVariable int id) {
        userService.getByIdOrThrow(userId);
        return service.getByIdOrThrow(id);
    }

    @GetMapping
    public List<Item> getAllByUser(@RequestHeader(USER_ID_HEADER) @NotNull int userId) {
        User owner = userService.getByIdOrThrow(userId);

        return service.getAllByUser(owner);
    }

    @GetMapping("/search")
    public List<Item> search(@RequestHeader(USER_ID_HEADER) @NotNull int userId, @RequestParam(value = "text") String searchText) {
        userService.getByIdOrThrow(userId);

        return service.search(searchText);
    }

}
