package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.item.ItemShort;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookingResponseDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemShort item;
    private UserDto booker;
    private Status status;
}
