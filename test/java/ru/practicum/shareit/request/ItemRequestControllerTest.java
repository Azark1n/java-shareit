package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestExtraDto;
import ru.practicum.shareit.request.dto.ItemRequestMapperImpl;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {
    @MockBean
    ItemRequestService itemRequestService;

    @Spy
    ItemRequestMapperImpl itemRequestMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    private User requester;
    private LocalDateTime nowPlus1Hour;
    private LocalDateTime nowPlus1day;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private ItemRequestExtraDto itemRequestExtraDto;


    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        nowPlus1Hour = now.plusHours(1);
        nowPlus1day = now.plusDays(1);

        requester = User.builder()
                .id(1)
                .name("user")
                .email("user@mail.com")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1)
                .description("Request")
                .requester(requester)
                .created(now)
                .build();
        itemRequestDto = itemRequestMapper.toDto(itemRequest);
        itemRequestExtraDto = itemRequestMapper.toExtraDto(itemRequest, new ArrayList<>());
    }

    @SneakyThrows
    @Test
    void create() {
        when(itemRequestService.create(any(), anyInt())).thenReturn(itemRequestDto);

        ItemRequestDto itemRequestForCreateDto = ItemRequestDto.builder()
                .description("Request")
                .build();
        mockMvc.perform(post("/requests")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", requester.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestForCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()));
    }

    @SneakyThrows
    @Test
    void getAllByUser() {
        when(itemRequestService.getAllByUserId(anyInt())).thenReturn(List.of(itemRequestExtraDto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(itemRequestExtraDto))));
    }

    @SneakyThrows
    @Test
    void getById() {
        when(itemRequestService.getById(anyInt(), anyInt())).thenReturn(itemRequestExtraDto);

        mockMvc.perform(get("/requests/{requestId}", itemRequestExtraDto.getId())
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemRequestExtraDto)));
    }

    @SneakyThrows
    @Test
    void getAll() {
        when(itemRequestService.getAll(anyInt(), any(), any())).thenReturn(List.of(itemRequestExtraDto));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(itemRequestExtraDto))));
    }
}