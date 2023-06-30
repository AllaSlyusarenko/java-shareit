package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.Generated;

@Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RequestDto {
    private Long id;
    private String description;

    public interface NewRequest {
    }
}