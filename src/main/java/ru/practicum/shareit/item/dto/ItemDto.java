package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.Generated;

import javax.validation.constraints.*;

@Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemDto {
    @Null(groups = {NewItem.class})
    private Long id;

    @NotBlank(groups = {NewItem.class})
    private String name;

    @Size(max = 200)
    @NotBlank(groups = {NewItem.class})
    private String description;

    @NotNull(groups = {NewItem.class})
    private Boolean available;

    private Long requestId;
}