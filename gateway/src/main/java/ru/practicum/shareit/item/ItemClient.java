package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(ItemDto dto, int userId) {
        return post("", userId, dto);
    }

    public ResponseEntity<Object> patch(int itemId, int userId, Map<String, Object> patchValues) {
        return patch("/" + itemId, userId, patchValues);
    }

    public ResponseEntity<Object> getById(int itemId, int userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> createComment(CommentDto commentDto, int id, int userId) {
        return post("/" + id + "/comment", userId, commentDto);
    }

    public ResponseEntity<Object> getAllByUserId(int userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> search(String searchText, int userId) {
        return get("/search?text={text}", userId, Map.of("text", searchText));
    }
}
