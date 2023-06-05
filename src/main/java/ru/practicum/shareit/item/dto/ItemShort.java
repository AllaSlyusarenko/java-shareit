package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.comment.CommentResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemShort {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingShort lastBooking;
    private BookingShort nextBooking;
    private List<CommentResponse> comments;
}
