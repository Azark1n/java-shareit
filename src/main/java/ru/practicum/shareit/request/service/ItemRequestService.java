package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto dto, int userId);

    List<ItemRequestDto> getAllByUserId(int userId);

    List<ItemRequestDto> getAll(int userId, Integer from, Integer size);
}
