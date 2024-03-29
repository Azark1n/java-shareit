package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestExtraDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static ru.practicum.shareit.Constants.USER_ID_HEADER;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDto create(@RequestHeader(USER_ID_HEADER) int userId, @RequestBody ItemRequestDto dto) {
        return service.create(dto, userId);
    }

    @GetMapping
    public List<ItemRequestExtraDto> getAllByUser(@RequestHeader(USER_ID_HEADER) int userId) {
        return service.getAllByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestExtraDto getById(@RequestHeader(USER_ID_HEADER) int userId, @PathVariable int requestId) {
        return service.getById(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestExtraDto> getAll(@RequestHeader(USER_ID_HEADER) int userId,
                                            @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return service.getAll(userId, from, size);
    }
}
