package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapperImpl;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {
    @MockBean
    BookingService bookingService;

    @Spy
    BookingMapperImpl bookingMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private User booker;
    private User owner;
    private Item item;
    private LocalDateTime nowPlus1Hour;
    private LocalDateTime nowPlus1day;
    private Booking booking;
    private BookingDto bookingDto;
    private BookingDto bookingDtoForCreate;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        nowPlus1Hour = now.plusHours(1);
        nowPlus1day = now.plusDays(1);

        booker = User.builder()
                .id(1)
                .name("booker")
                .email("booker@mail.com")
                .build();

        owner = User.builder()
                .id(1)
                .name("owner")
                .email("owner@mail.com")
                .build();

        item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .owner(owner)
                .available(true)
                .build();

        booking = Booking.builder()
                .id(1)
                .item(item)
                .booker(booker)
                .start(nowPlus1Hour)
                .end(nowPlus1day)
                .status(BookingStatus.WAITING)
                .build();
        bookingDto = bookingMapper.toDto(booking);

        bookingDtoForCreate = BookingDto.builder()
                .itemId(1)
                .start(nowPlus1Hour)
                .end(nowPlus1day)
                .build();
    }

    @SneakyThrows
    @Test
    void create() {
        when(bookingService.create(any(), anyInt())).thenReturn(bookingDto);

        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", booker.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDtoForCreate)))

                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void patchTest() {
        when(bookingService.patch(anyInt(), anyInt(), anyBoolean())).thenReturn(bookingDto);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingDto.getId())
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", owner.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void getTest() {
        when(bookingService.getById(anyInt(), anyInt())).thenReturn(bookingDto);

        String result = mockMvc.perform(get("/bookings/{bookingId}", bookingDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", booker.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void getAllByBooker() {
        when(bookingService.getAllByBookerAndState(anyInt(), any(), any(), any())).thenReturn(List.of(bookingDto));

        String result = mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", owner.getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(bookingDto)), result);
    }

    @SneakyThrows
    @Test
    void getAllByOwner() {
        when(bookingService.getAllByOwnerAndState(anyInt(), any(), any(), any())).thenReturn(List.of(bookingDto));

        String result = mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", owner.getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(bookingDto)), result);
    }
}