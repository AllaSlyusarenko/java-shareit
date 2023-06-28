package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.item.ItemDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RequestResponseGetDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items = new ArrayList<>();
}
