package ru.practicum.shareit.request.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestExtraDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@Data
@RequiredArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;

    @Override
    public ItemRequestDto create(ItemRequestDto dto, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        ItemRequest itemRequest = itemRequestMapper.toModel(dto, user);
        log.info(String.format("Create item request: %s", itemRequest));

        return itemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestExtraDto> getAllByUserId(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        return itemRequestRepository.findByRequesterOrderByCreatedDesc(user).stream()
                .map(request -> {
                    List<ItemDto> itemsDto = itemRepository.findByRequest(request).stream().map(itemMapper::toDto).collect(Collectors.toList());
                    return itemRequestMapper.toExtraDto(request, itemsDto);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestExtraDto getById(int userId, int requestId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(String.format("Request with id %d not found", requestId)));
        List<ItemDto> itemsDto = itemRepository.findByRequest(itemRequest).stream().map(itemMapper::toDto).collect(Collectors.toList());

        return itemRequestMapper.toExtraDto(itemRequest, itemsDto);
    }

    @Override
    public List<ItemRequestExtraDto> getAll(int userId, Integer from, Integer size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));

        Pageable pageable = PageRequest.of(from / size, size);
        return itemRequestRepository.findByRequesterNotOrderByCreatedDesc(user, pageable).stream()
                .map(request -> {
                    List<ItemDto> itemsDto = itemRepository.findByRequest(request).stream().map(itemMapper::toDto).collect(Collectors.toList());
                    return itemRequestMapper.toExtraDto(request, itemsDto);
                })
                .collect(Collectors.toList());
    }
}
