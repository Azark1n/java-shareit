package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Value
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    UserClient client;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
        log.info("Creating user {}", userDto);
        return client.create(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patch(@PathVariable int id, @RequestBody Map<String,Object> patchValues) {
        log.info("Patching userId={}, patchValues={}", id, patchValues);
        return client.patch(id, patchValues);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable int id) {
        return client.getById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable int id) {
        return client.deleteById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Getting all");
        return client.getAll();
    }
}
