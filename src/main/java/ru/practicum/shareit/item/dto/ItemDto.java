package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.*;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class ItemDto {
    @Null(groups = {UserDto.New.class})
    @JsonProperty("id")
    private long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private boolean available;
    @NotNull
    private User owner; // или DTO
    private ItemRequest request;// если создан по запросу, тут ссылка на запрос
}
