package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.UnsupportedStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.Constants.USER_ID_HEADER;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
	private final BookingClient client;

	@PostMapping
	public ResponseEntity<Object> create(@RequestHeader(USER_ID_HEADER) int userId, @Valid @RequestBody BookingDto dto) {
		if (!dto.getStart().isBefore(dto.getEnd())) {
			throw new BadRequestException("Can't create booking where start is not before end");
		}
		log.info("Creating booking {}, userId={}", dto, userId);
		return client.create(dto, userId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> patch(@RequestHeader(USER_ID_HEADER) int userId, @PathVariable int bookingId, @RequestParam boolean approved) {
		log.info("Patching bookingId={}, userId={}, approved={}", bookingId, userId, approved);
		return client.patch(bookingId, userId, approved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> get(@RequestHeader(USER_ID_HEADER) int userId, @PathVariable int bookingId) {
		return client.getById(bookingId, userId);
	}

	@GetMapping
	public ResponseEntity<Object> getAllByBooker(@RequestHeader(USER_ID_HEADER) int userId,
			@RequestParam(name = "state", defaultValue = "ALL") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new UnsupportedStatusException("Unknown state: " + stateParam));
		log.info("Getting all by bookerId={}", userId);
		return client.getAllByBookerAndState(userId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getAllByOwner(@RequestHeader(USER_ID_HEADER) int userId,
			@RequestParam(name = "state", defaultValue = "ALL") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new UnsupportedStatusException("Unknown state: " + stateParam));
		log.info("Getting all by ownerId={}", userId);
		return client.getAllByOwnerAndState(userId, state, from, size);
	}
}
