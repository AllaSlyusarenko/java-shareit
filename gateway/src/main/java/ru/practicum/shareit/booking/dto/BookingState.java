package ru.practicum.shareit.booking.dto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.UnknownStateException;

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
    WAITING;

    @Component
    public class StringToBookingState implements Converter<String, BookingState> {
        @Override
        public BookingState convert(String state) {
            try {
                return BookingState.valueOf(state.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new UnknownStateException(state);
            }
        }
    }
}