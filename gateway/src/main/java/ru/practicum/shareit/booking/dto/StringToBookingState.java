package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;
import ru.practicum.shareit.exception.UnknownStateException;

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