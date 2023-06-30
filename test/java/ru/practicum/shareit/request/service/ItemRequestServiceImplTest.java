package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemMapperImpl;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestExtraDto;
import ru.practicum.shareit.request.dto.ItemRequestMapperImpl;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock ItemRequestRepository itemRequestRepository;
    @Mock UserRepository userRepository;
    @Mock ItemRepository itemRepository;
    @Spy ItemRequestMapperImpl itemRequestMapper;
    @Spy ItemMapperImpl itemMapper;

    @InjectMocks ItemRequestServiceImpl itemRequestService;

    private User user1;
    private ItemRequest itemRequest1;
    private ItemRequestDto itemRequest1Dto;
    private ItemRequestDto itemRequest1NoIdDto;
    private ItemRequest itemRequest2;
    private ItemRequestDto itemRequest2Dto;
    private ItemRequestExtraDto itemRequest1ExtraDto;
    private ItemRequestExtraDto itemRequest2ExtraDto;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .id(1)
                .name("user1")
                .email("user1@mail.ru")
                .build();

        itemRequest1 = ItemRequest.builder()
                .id(1)
                .description("description1")
                .requester(user1)
                .created(LocalDateTime.now())
                .build();
        itemRequest1Dto = itemRequestMapper.toDto(itemRequest1);
        itemRequest1ExtraDto = itemRequestMapper.toExtraDto(itemRequest1, new ArrayList<>());
        itemRequest1NoIdDto = itemRequest1Dto.toBuilder().id(null).build();

        itemRequest2 = ItemRequest.builder()
                .id(2)
                .description("description2")
                .requester(user1)
                .created(LocalDateTime.now())
                .build();
        itemRequest2Dto = itemRequestMapper.toDto(itemRequest2);
        itemRequest2ExtraDto = itemRequestMapper.toExtraDto(itemRequest2, new ArrayList<>());
    }

    @Test
    void create() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemRequestService.create(itemRequest1NoIdDto, 1)).isInstanceOf(NotFoundException.class);

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(itemRequestRepository.save(Mockito.any())).thenReturn(itemRequest1);
        assertThat(itemRequestService.create(itemRequest1NoIdDto, 1)).isEqualTo(itemRequest1Dto);

    }

    @Test
    void getAllByUserId() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemRequestService.getAllByUserId(1)).isInstanceOf(NotFoundException.class);

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(itemRequestRepository.findByRequesterOrderByCreatedDesc(user1)).thenReturn(List.of(itemRequest1, itemRequest2));
        when(itemRepository.findByRequest(Mockito.any())).thenReturn(new ArrayList<>());
        assertThat(itemRequestService.getAllByUserId(1)).isEqualTo(List.of(itemRequest1ExtraDto, itemRequest2ExtraDto));
    }

    @Test
    void getById() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemRequestService.getById(1, 1)).isInstanceOf(NotFoundException.class);

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(itemRequestRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemRequestService.getById(1, 1)).isInstanceOf(NotFoundException.class);

        when(itemRequestRepository.findById(1)).thenReturn(Optional.of(itemRequest1));
        when(itemRepository.findByRequest(Mockito.any())).thenReturn(new ArrayList<>());
        assertThat(itemRequestService.getById(1, 1)).isEqualTo(itemRequest1ExtraDto);
    }

    @Test
    void getAll() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemRequestService.getAll(1, 0, 10)).isInstanceOf(NotFoundException.class);

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(itemRequestRepository.findByRequesterNotOrderByCreatedDesc(Mockito.any(), Mockito.any())).thenReturn(List.of(itemRequest1, itemRequest2));
        when(itemRepository.findByRequest(Mockito.any())).thenReturn(new ArrayList<>());
        assertThat(itemRequestService.getAll(1, 0, 10)).isEqualTo(List.of(itemRequest1ExtraDto, itemRequest2ExtraDto));
    }
}