package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Item {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private Long owner; // userId
    private ItemRequest request;// если создан по запросу, тут ссылка на запрос
}
