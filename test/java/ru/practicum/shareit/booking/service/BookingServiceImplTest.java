package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapperImpl;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock BookingRepository bookingRepository;
    @Mock UserRepository userRepository;
    @Mock ItemRepository itemRepository;
    @Spy BookingMapperImpl bookingMapper;
    @InjectMocks BookingServiceImpl bookingService;

    private User user1;
    private User user2;
    private Item item1;
    private Booking booking1;
    private BookingDto booking1Dto;
    private BookingDto booking1NoIdDto;

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

        item1 = Item.builder()
                .id(1)
                .name("item1")
                .description("description1")
                .available(true)
                .owner(user1)
                .build();

        booking1 = Booking.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(item1)
                .booker(user1)
                .status(BookingStatus.WAITING)
                .build();
        booking1Dto = bookingMapper.toDto(booking1);
        booking1NoIdDto = booking1Dto.toBuilder().id(null).build();
    }

    @Test
    void create() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookingService.create(booking1NoIdDto, 1)).isInstanceOf(ForbiddenException.class);

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(itemRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookingService.create(booking1NoIdDto, 1)).isInstanceOf(NotFoundException.class);

        item1.setAvailable(false);
        when(itemRepository.findById(1)).thenReturn(Optional.of(item1));
        assertThatThrownBy(() -> bookingService.create(booking1NoIdDto, 1)).isInstanceOf(BadRequestException.class);

        item1.setAvailable(true);
        when(itemRepository.findById(1)).thenReturn(Optional.of(item1));
        booking1NoIdDto.setEnd(booking1Dto.getStart().minusDays(1));
        assertThatThrownBy(() -> bookingService.create(booking1NoIdDto, 1)).isInstanceOf(BadRequestException.class);

        booking1NoIdDto.setEnd(booking1Dto.getStart().plusDays(1));
        assertThatThrownBy(() -> bookingService.create(booking1NoIdDto, 1)).isInstanceOf(NotFoundException.class);

        item1.setOwner(user2);
        when(itemRepository.findById(1)).thenReturn(Optional.of(item1));
        when(bookingRepository.save(Mockito.any())).thenReturn(booking1);
        assertThat(bookingService.create(booking1NoIdDto, 1)).isEqualTo(booking1Dto);
    }

    @Test
    void patch() {
        when(bookingRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookingService.patch(1, 1, true)).isInstanceOf(NotFoundException.class);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking1));
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookingService.patch(1, 1, true)).isInstanceOf(NotFoundException.class);

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        booking1.getItem().setOwner(user2);
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking1));
        assertThatThrownBy(() -> bookingService.patch(1, 1, true)).isInstanceOf(NotFoundException.class);

        booking1.getItem().setOwner(user1);
        booking1.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking1));
        assertThatThrownBy(() -> bookingService.patch(1, 1, true)).isInstanceOf(BadRequestException.class);

        booking1.setStatus(BookingStatus.WAITING);
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking1));
        Booking bookingPatched = booking1.toBuilder().status(BookingStatus.APPROVED).build();
        when(bookingRepository.save(Mockito.any())).thenReturn(bookingPatched);
        booking1Dto.setStatus(BookingStatus.APPROVED);
        assertThat(bookingService.patch(1, 1, true)).isEqualTo(booking1Dto);
    }

    @Test
    void getById() {
        when(bookingRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookingService.getById(1, 1)).isInstanceOf(NotFoundException.class);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking1));
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookingService.getById(1, 1)).isInstanceOf(NotFoundException.class);

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking1));
        assertThatThrownBy(() -> bookingService.getById(1, 2)).isInstanceOf(NotFoundException.class);

        booking1.getItem().setOwner(user1);
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking1));
        assertThat(bookingService.getById(1, 1)).isEqualTo(booking1Dto);
    }

    @Test
    void getAllByBookerAndState() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookingService.getAllByBookerAndState(1, "current", 0, 1)).isInstanceOf(ForbiddenException.class);

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        assertThatThrownBy(() -> bookingService.getAllByBookerAndState(1, "qwe", 0, 1)).isInstanceOf(UnsupportedStatusException.class);

        when(bookingRepository.findByBookerOrderByStartDesc(Mockito.any(), Mockito.any())).thenReturn(List.of(booking1));
        assertThat(bookingService.getAllByBookerAndState(1, "ALL", 0, 1)).isEqualTo(List.of(booking1Dto));

        when(bookingRepository.findByBookerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(Mockito.any(), Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(List.of(booking1));
        assertThat(bookingService.getAllByBookerAndState(1, "CURRENT", 0, 1)).isEqualTo(List.of(booking1Dto));

        when(bookingRepository.findByBookerAndEndLessThanOrderByStartDesc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of(booking1));
        assertThat(bookingService.getAllByBookerAndState(1, "PAST", 0, 1)).isEqualTo(List.of(booking1Dto));

        when(bookingRepository.findByBookerAndStartGreaterThanOrderByStartDesc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of(booking1));
        assertThat(bookingService.getAllByBookerAndState(1, "FUTURE", 0, 1)).isEqualTo(List.of(booking1Dto));

        when(bookingRepository.findByBookerAndStatusOrderByStartDesc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of(booking1));
        assertThat(bookingService.getAllByBookerAndState(1, "APPROVED", 0, 1)).isEqualTo(List.of(booking1Dto));
    }

    @Test
    void getAllByOwnerAndState() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookingService.getAllByOwnerAndState(1, "current", 0, 1)).isInstanceOf(ForbiddenException.class);

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        assertThatThrownBy(() -> bookingService.getAllByOwnerAndState(1, "qwe", 0, 1)).isInstanceOf(UnsupportedStatusException.class);

        when(bookingRepository.findByItem_OwnerOrderByStartDesc(Mockito.any(), Mockito.any())).thenReturn(List.of(booking1));
        assertThat(bookingService.getAllByOwnerAndState(1, "ALL", 0, 1)).isEqualTo(List.of(booking1Dto));

        when(bookingRepository.findByItem_OwnerAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(Mockito.any(), Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(List.of(booking1));
        assertThat(bookingService.getAllByOwnerAndState(1, "CURRENT", 0, 1)).isEqualTo(List.of(booking1Dto));

        when(bookingRepository.findByItem_OwnerAndEndLessThanOrderByStartDesc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of(booking1));
        assertThat(bookingService.getAllByOwnerAndState(1, "PAST", 0, 1)).isEqualTo(List.of(booking1Dto));

        when(bookingRepository.findByItem_OwnerAndStartGreaterThanOrderByStartDesc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of(booking1));
        assertThat(bookingService.getAllByOwnerAndState(1, "FUTURE", 0, 1)).isEqualTo(List.of(booking1Dto));

        when(bookingRepository.findByItem_OwnerAndStatusOrderByStartDesc(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(List.of(booking1));
        assertThat(bookingService.getAllByOwnerAndState(1, "APPROVED", 0, 1)).isEqualTo(List.of(booking1Dto));
    }
}