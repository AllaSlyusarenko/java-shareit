package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemDto {
    private long id;

    @NotBlank(groups = {NewItem.class})
    private String name;

    @Size(max = 200)
    @NotBlank(groups = {NewItem.class})
    private String description;

    @NotNull(groups = {NewItem.class})
    private Boolean available;

    private Long owner; // userId

    private ItemRequest request;// если создан по запросу, тут ссылка на запрос
}
