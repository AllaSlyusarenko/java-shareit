package ru.practicum.shareit.item.dto.item;

import lombok.*;
import ru.practicum.shareit.marker.Create;

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
    @Null(groups = {Create.class})
    private Long id;

    @NotBlank(groups = {Create.class})
    private String name;

    @Size(max = 200)
    @NotBlank(groups = {Create.class})
    private String description;

    @NotNull(groups = {Create.class})
    private Boolean available;

    private Long requestId;
}