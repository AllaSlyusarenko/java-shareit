package ru.practicum.shareit.booking;

import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
public class Booking {
    @Positive
    private long id;
    @FutureOrPresent
    private LocalDateTime start;
    @FutureOrPresent
    private LocalDateTime end;
    @NotNull
    private Item item; // или DTO
    @NotNull
    private User booker; // или DTO
    @NotNull
    private Status status; // или DTO


    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private String available;//возможно boolean, возможно enum
    @NotNull
    private User owner; // DTO
    private ItemRequest request;// если создан по запросу, тут ссылка на запрос

}
