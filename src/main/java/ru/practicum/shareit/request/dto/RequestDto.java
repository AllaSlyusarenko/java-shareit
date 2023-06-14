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
public class RequestDto {
    @Null(groups = {NewRequest.class})
    private Long id;

    @Size(max = 200)
    @NotBlank(groups = {NewRequest.class})
    private String description;

    public interface NewRequest {
    }
}