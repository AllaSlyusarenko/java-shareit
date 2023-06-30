package ru.practicum.shareit.item.dto.item;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

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