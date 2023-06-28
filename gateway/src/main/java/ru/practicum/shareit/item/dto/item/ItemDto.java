package ru.practicum.shareit.item.dto.item;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemDto {
    @Null
    private Long id;

    @NotBlank
    private String name;

    @Size(max = 200)
    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    private Long requestId;
}