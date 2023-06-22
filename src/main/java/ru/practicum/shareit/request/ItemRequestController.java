package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Value
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    ItemRequestService service;

    @PostMapping
    public ItemRequestDto create(@RequestHeader(USER_ID_HEADER) @NotNull int userId,
                                 @Validated(ItemRequestDto.Create.class) @RequestBody ItemRequestDto dto) {
        return service.create(dto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getAllByUser(@RequestHeader(USER_ID_HEADER) @NotNull int userId) {
        return service.getAllByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader(USER_ID_HEADER) @NotNull int userId,
                                       @RequestParam(name = "from", defaultValue = "0") Integer from,
                                       @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return service.getAll(userId, from, size);
    }
}
