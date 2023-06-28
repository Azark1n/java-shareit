package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestExtraDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDto create(@RequestHeader(USER_ID_HEADER) @NotNull int userId,
                                 @Validated(ItemRequestDto.Create.class) @RequestBody ItemRequestDto dto) {
        return service.create(dto, userId);
    }

    @GetMapping
    public List<ItemRequestExtraDto> getAllByUser(@RequestHeader(USER_ID_HEADER) @NotNull int userId) {
        return service.getAllByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestExtraDto getById(@RequestHeader(USER_ID_HEADER) @NotNull int userId,
                                       @PathVariable int requestId) {
        return service.getById(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestExtraDto> getAll(@RequestHeader(USER_ID_HEADER) @NotNull int userId,
                                            @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                            @RequestParam(name = "size", defaultValue = "20") @Min(1) Integer size) {
        return service.getAll(userId, from, size);
    }
}
