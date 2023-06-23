package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtraDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Value
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    ItemService service;

    @PostMapping
    public ItemDto create(@RequestHeader(USER_ID_HEADER) @NotNull int userId, @Validated(ItemDto.Create.class) @RequestBody ItemDto dto) {
        return service.create(dto, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto patch(@RequestHeader(USER_ID_HEADER) @NotNull int userId, @PathVariable int id, @Validated(ItemDto.Create.class) @RequestBody Map<String, Object> patchValues) {
        return service.patch(id, userId, patchValues);
    }

    @GetMapping("/{id}")
    public ItemExtraDto getById(@RequestHeader(USER_ID_HEADER) @NotNull int userId, @PathVariable int id) {
        return service.getById(id, userId);
    }

    @PostMapping("/{id}/comment")
    public CommentDto createComment(@RequestHeader(USER_ID_HEADER) @NotNull int userId, @PathVariable int id,
                                       @RequestBody @Valid CommentDto commentDto) {
        return service.createComment(commentDto, id, userId);
    }

    @GetMapping
    public List<ItemExtraDto> getAllByUser(@RequestHeader(USER_ID_HEADER) @NotNull int userId) {
        return service.getAllByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(USER_ID_HEADER) @NotNull int userId, @RequestParam(value = "text") String searchText) {
        return service.search(searchText, userId);
    }

}
