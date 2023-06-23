package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestExtraDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto dto, int userId);

    List<ItemRequestExtraDto> getAllByUserId(int userId);

    ItemRequestExtraDto getById(int userId, int requestId);

    List<ItemRequestExtraDto> getAll(int userId, Integer from, Integer size);
}
