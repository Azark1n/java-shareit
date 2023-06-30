package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtraDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;
import java.util.Map;

import static ru.practicum.shareit.Constants.USER_ID_HEADER;

@Value
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    ItemService service;

    @PostMapping
    public ItemDto create(@RequestHeader(USER_ID_HEADER) int userId, @RequestBody ItemDto dto) {
        return service.create(dto, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto patch(@RequestHeader(USER_ID_HEADER) int userId, @PathVariable int id, @RequestBody Map<String, Object> patchValues) {
        return service.patch(id, userId, patchValues);
    }

    @GetMapping("/{id}")
    public ItemExtraDto getById(@RequestHeader(USER_ID_HEADER) int userId, @PathVariable int id) {
        return service.getById(id, userId);
    }

    @PostMapping("/{id}/comment")
    public CommentDto createComment(@RequestHeader(USER_ID_HEADER) int userId, @PathVariable int id, @RequestBody CommentDto commentDto) {
        return service.createComment(commentDto, id, userId);
    }

    @GetMapping
    public List<ItemExtraDto> getAllByUser(@RequestHeader(USER_ID_HEADER) int userId) {
        return service.getAllByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(USER_ID_HEADER) int userId, @RequestParam(value = "text") String searchText) {
        return service.search(searchText, userId);
    }

}
