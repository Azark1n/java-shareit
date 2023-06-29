package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.Validator;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingMapperImpl;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock ItemRepository itemRepository;
    @Mock UserRepository userRepository;
    @Mock BookingRepository bookingRepository;
    @Mock CommentRepository commentRepository;
    @Mock ItemRequestRepository itemRequestRepository;
    @Spy Validator validator;
    @Spy ItemMapperImpl itemMapper;
    @Spy BookingMapperImpl bookingMapper;
    @Spy CommentMapperImpl commentMapper;
    @InjectMocks ItemServiceImpl itemService;

    private User user1;
    private Item item1NoId;
    private ItemDto item1NoIdDto;
    private Item item1;
    private ItemDto item1Dto;
    private User user2;
    private ItemRequest request1;
    private ItemExtraDto item1ExtraDto;
    private Item item2;
    private ItemDto item2Dto;
    private ItemExtraDto item2ExtraDto;
    private Comment comment1;
    private CommentDto comment1Dto;
    private CommentDto comment1NoIdDto;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .id(1)
                .name("user1")
                .email("user1@mail.ru")
                .build();
        user2 = User.builder()
                .id(2)
                .name("user2")
                .email("user2@mail.ru")
                .build();

        request1 = ItemRequest.builder()
                .id(1)
                .description("request1")
                .requester(user2)
                .build();

        item1NoId = Item.builder()
                .name("item1")
                .description("description1")
                .available(true)
                .owner(user1)
                .request(request1)
                .build();
        item1NoIdDto = itemMapper.toDto(item1NoId);

        item1 = item1NoId.toBuilder().id(1).build();
        item1Dto = itemMapper.toDto(item1);

        item1ExtraDto = itemMapper.toExtraDto(item1, null, null, new ArrayList<>());

        item2 = Item.builder()
                .id(2)
                .name("item2")
                .description("description2")
                .available(true)
                .owner(user1)
                .build();
        item2Dto = itemMapper.toDto(item2);
        item2ExtraDto = itemMapper.toExtraDto(item2, null, null, new ArrayList<>());

        comment1 = Comment.builder()
                .id(1)
                .text("comment1")
                .item(item1)
                .author(user1)
                .build();
        comment1Dto = commentMapper.toDto(comment1);
        comment1NoIdDto = comment1Dto.toBuilder().id(null).build();
    }

    @Test
    void create() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(itemRepository.save(item1NoId)).thenReturn(item1);
        when(itemRequestRepository.findById(1)).thenReturn(Optional.of(request1));
        assertThat(itemService.create(item1NoIdDto, 1)).isEqualTo(item1Dto);

        when(userRepository.findById(2)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemService.create(item1NoIdDto, 2)).isInstanceOf(NotFoundException.class);

        when(itemRequestRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemService.create(item1NoIdDto, 1)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void patch() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item1));
        when(itemRequestRepository.findById(1)).thenReturn(Optional.of(request1));

        Item item1Patched = item1.toBuilder().description("upd").build();
        ItemDto item1PatchedDto = itemMapper.toDto(item1Patched);
        when(itemRepository.save(item1Patched)).thenReturn(item1Patched);
        assertThat(itemService.patch(1, 1, Map.of("description", "upd"))).isEqualTo(item1PatchedDto);

        when(itemRepository.findById(2)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemService.patch(2, 1, Map.of())).isInstanceOf(NotFoundException.class);

        when(userRepository.findById(3)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemService.patch(1, 3, Map.of())).isInstanceOf(NotFoundException.class);

        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        assertThatThrownBy(() -> itemService.patch(1, 2, Map.of())).isInstanceOf(ForbiddenException.class);

        when(itemRequestRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemService.patch(1, 1, Map.of())).isInstanceOf(NotFoundException.class);
    }

    @Test
    void getById() {
        when(itemRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemService.getById(1, 1)).isInstanceOf(NotFoundException.class);

        when(itemRepository.findById(1)).thenReturn(Optional.of(item1));
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemService.getById(1, 1)).isInstanceOf(NotFoundException.class);

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        assertThat(itemService.getById(1, 1)).isEqualTo(item1ExtraDto);
    }

    @Test
    void getAllByUserId() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemService.getAllByUserId(1)).isInstanceOf(NotFoundException.class);

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(itemRepository.findByOwnerOrderById(user1)).thenReturn(List.of(item1, item2));
        assertThat(itemService.getAllByUserId(1)).isEqualTo(List.of(item1ExtraDto, item2ExtraDto));
    }

    @Test
    void search() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemService.search("", 1)).isInstanceOf(NotFoundException.class);

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        assertThat(itemService.search("", 1)).isEqualTo(new ArrayList<>());

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(itemRepository.findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCase("item")).thenReturn(List.of(item1, item2));
        assertThat(itemService.search("item", 1)).isEqualTo(List.of(item1Dto, item2Dto));
    }

    @Test
    void createComment() {
        when(itemRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemService.createComment(comment1Dto, 1, 1)).isInstanceOf(NotFoundException.class);

        when(itemRepository.findById(1)).thenReturn(Optional.of(item1));
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemService.createComment(comment1Dto, 1, 1)).isInstanceOf(NotFoundException.class);

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(bookingRepository.findFirstByItemAndBookerAndStatusAndStartBefore(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> itemService.createComment(comment1Dto, 1, 1)).isInstanceOf(BadRequestException.class);

        when(bookingRepository.findFirstByItemAndBookerAndStatusAndStartBefore(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(new Booking()));
        when(commentRepository.save(Mockito.any())).thenReturn(comment1);
        assertThat(itemService.createComment(comment1NoIdDto, 1, 1)).isEqualTo(comment1Dto);

    }
}