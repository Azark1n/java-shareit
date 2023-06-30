package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtraDto;
import ru.practicum.shareit.item.dto.ItemMapperImpl;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {
    @MockBean
    ItemService itemService;

    @Spy
    ItemMapperImpl itemMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private User owner;
    private User booker;
    private Item item;
    private ItemDto itemDto;
    private ItemExtraDto itemExtraDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1)
                .name("owner")
                .email("owner@mail.com")
                .build();

        booker = User.builder()
                .id(2)
                .name("booker")
                .email("booker@mail.com")
                .build();

        item = Item.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .owner(owner)
                .build();
        itemDto = itemMapper.toDto(item);
        itemExtraDto = itemMapper.toExtraDto(item, null, null, new ArrayList<>());

        commentDto = CommentDto.builder()
                .id(1)
                .text("comment")
                .authorName("userName")
                .build();
    }

    @SneakyThrows
    @Test
    void create() {
        when(itemService.create(any(), anyInt())).thenReturn(itemDto);

        ItemDto itemDtoNoId = itemDto.toBuilder().id(null).build();
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDtoNoId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.name").value(item.getName()));
    }

    @SneakyThrows
    @Test
    void patchTest() {
        when(itemService.patch(anyInt(), anyInt(), any())).thenReturn(itemDto);

        mockMvc.perform(patch("/items/{itemId}", itemDto.getId())
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.name").value(item.getName()));
    }

    @SneakyThrows
    @Test
    void getById() {
        when(itemService.getById(anyInt(), anyInt())).thenReturn(itemExtraDto);

        mockMvc.perform(get("/items/{id}", itemDto.getId())
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.name").value(item.getName()));
    }

    @SneakyThrows
    @Test
    void createComment() {
        when(itemService.createComment(any(), anyInt(), anyInt())).thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", item.getId())
                        .header("X-Sharer-User-Id", "1")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()));

    }

    @SneakyThrows
    @Test
    void getAllByUser() {
        when(itemService.getAllByUserId(anyInt())).thenReturn(List.of(itemExtraDto));
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(item.getId()))
                .andExpect(jsonPath("$[0].description").value(item.getDescription()))
                .andExpect(jsonPath("$[0].name").value(item.getName()));
    }

    @SneakyThrows
    @Test
    void search() {
        when(itemService.search(any(), anyInt())).thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search")
                        .param("text", "some")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(itemDto))));
    }
}