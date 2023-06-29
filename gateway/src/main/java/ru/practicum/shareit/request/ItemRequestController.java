package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_ID_HEADER) int userId, @Valid @RequestBody ItemRequestDto dto) {
        log.info("Creating itemRequest {}, userId={}", dto, userId);
        return client.create(dto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUserId(@RequestHeader(USER_ID_HEADER) int userId) {
        return client.getAllByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader(USER_ID_HEADER) int userId, @PathVariable int requestId) {
        return client.getById(requestId, userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader(USER_ID_HEADER) int userId,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return client.getAll(userId, from, size);
    }
}
