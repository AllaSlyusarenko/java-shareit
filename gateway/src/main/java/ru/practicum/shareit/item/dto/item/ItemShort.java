package ru.practicum.shareit.item.dto.item;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.dto.comment.CommentResponse;

import java.util.List;

@Generated
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