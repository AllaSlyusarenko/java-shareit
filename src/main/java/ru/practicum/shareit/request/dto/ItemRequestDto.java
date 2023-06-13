package ru.practicum.shareit.request.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemRequestDto {
    @Null(groups = {NewItemRequest.class})
    private Long id;

    @Size(max = 200)
    @NotBlank(groups = {NewItemRequest.class})
    private String description;

    public interface NewItemRequest {
    }
}