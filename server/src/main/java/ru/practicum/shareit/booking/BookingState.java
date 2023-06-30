package ru.practicum.shareit.booking;


import lombok.Generated;

import java.util.Optional;

@Generated
public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED;

    public static Optional<BookingState> from(String stringState) {
        for (BookingState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}