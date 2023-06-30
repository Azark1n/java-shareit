package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtraDto;

import java.util.List;
import java.util.Map;

public interface ItemService {
    ItemDto create(ItemDto dto, int userId);

    ItemDto patch(int id, int userId, Map<String, Object> patchValues);

    ItemExtraDto getById(int id, int userId);

    List<ItemExtraDto> getAllByUserId(int userId);

    List<ItemDto> search(String text, int userId);

    CommentDto createComment(CommentDto dto, int id, int userId);
}
