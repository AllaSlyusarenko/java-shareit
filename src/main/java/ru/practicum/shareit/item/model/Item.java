package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class Item {
    @Positive
    private long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private String available;//возможно boolean, возможно enum
    @NotNull
    private User owner; // или DTO
    private ItemRequest request;// если создан по запросу, тут ссылка на запрос
}
