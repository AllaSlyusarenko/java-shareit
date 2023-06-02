package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.NewItem;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookingDto {
    @Null(groups = {NewBooking.class})
    private Long id;

    @NotBlank(groups = {NewItem.class})
    @FutureOrPresent
    private String start;
    @Future
    @NotBlank(groups = {NewItem.class})
    private String end;

    @NotBlank(groups = {NewItem.class})
    private Item item;

    @NotBlank(groups = {NewItem.class})
    private User booker;

    @NotBlank(groups = {NewItem.class})
    private Status status;
}
