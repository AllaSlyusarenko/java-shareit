package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Item {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private Long owner; // userId
    private ItemRequest request;// если создан по запросу, тут ссылка на запрос
}
