package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.*;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemDto {

    //@Null(groups = {NewItem.class})
    private long id;

    @NotBlank(groups = {NewItem.class})
    @JsonProperty("name")
    private String name;

    @Size(max = 200)
    @NotBlank(groups = {NewItem.class})
    @JsonProperty("description")
    private String description;

    @NotNull(groups = {NewItem.class})
    @JsonProperty("available")
    private boolean available;

    @JsonProperty("owner")
    private Long owner; // userId

    @JsonProperty("request")
    private ItemRequest request;// если создан по запросу, тут ссылка на запрос

    public interface NewItem {
    }
}
