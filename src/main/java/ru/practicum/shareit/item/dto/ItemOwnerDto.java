package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingShort;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemOwnerDto {
    private Long id;

    @NotBlank
    private String name;

    @Size(max = 200)
    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    private Booking lastBooking;
    private Booking nextBooking;
}