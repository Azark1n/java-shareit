package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.Map;

import static ru.practicum.shareit.Constants.USER_ID_HEADER;

@Slf4j
@Value
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    ItemClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_ID_HEADER) int userId, @Valid @RequestBody ItemDto dto) {
        log.info("Creating item {}, userId={}", dto, userId);
        return client.create(dto, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patch(@RequestHeader(USER_ID_HEADER) int userId, @PathVariable int id, @RequestBody Map<String, Object> patchValues) {
        log.info("Patching itemId={}, userId={}, patchValues={}", id, userId, patchValues);
        return client.patch(id, userId, patchValues);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@RequestHeader(USER_ID_HEADER) int userId, @PathVariable int id) {
        log.info("Getting item by Id={}, userId={}", id, userId);
        return client.getById(id, userId);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(USER_ID_HEADER) int userId, @PathVariable int id, @Valid @RequestBody CommentDto commentDto) {
        log.info("Creating comment {}, itemId={}, userId={}", commentDto, id, userId);
        return client.createComment(commentDto, id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUserId(@RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Getting all by ownerId={}", userId);
        return client.getAllByUserId(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(USER_ID_HEADER) int userId, @RequestParam(value = "text") String searchText) {
        log.info("Searching item by text={}, userId={}", searchText, userId);
        return client.search(searchText, userId);
    }

}
