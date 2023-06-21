package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Value
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    BookingService service;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto create(@RequestHeader(USER_ID_HEADER) @NotNull int userId,
                          @Validated(BookingDto.Create.class) @RequestBody BookingDto dto) {
        return service.create(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto patch(@RequestHeader(USER_ID_HEADER) @NotNull int userId,
                         @PathVariable int bookingId, @RequestParam @NotNull boolean approved) {
        return service.patch(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto get(@RequestHeader(USER_ID_HEADER) @NotNull int userId, @PathVariable int bookingId) {
        return service.getById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllByBooker(@RequestHeader(USER_ID_HEADER) @NotNull int userId,
                                        @RequestParam(defaultValue = "ALL") String state) {
        return service.getAllByBookerAndState(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestHeader(USER_ID_HEADER) @NotNull int userId,
                                          @RequestParam(defaultValue = "ALL") String state) {
        return service.getAllByOwnerAndState(userId, state);
    }
}
