package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import ru.practicum.shareit.exception.UnknownStateException;

@AllArgsConstructor
public enum BookingState {
    // Все
    ALL,
    // Текущие
    CURRENT,
    // Будущие
    FUTURE,
    // Завершенные
    PAST,
    // Отклоненные
    REJECTED,
    // Ожидающие подтверждения
    WAITING,
    UNSUPPORTED_STATUS;

    public static BookingState stringToState(String state) {
        BookingState resultState = null;
        if (state != null) {
            for (BookingState bookingState : BookingState.values()) {
                if (state.equalsIgnoreCase(bookingState.name())) {
                    if (bookingState.equals(UNSUPPORTED_STATUS)) {
                        throw new UnknownStateException(state);
                    }
                    resultState = bookingState;
                }
            }
        }
        return resultState;
    }
}