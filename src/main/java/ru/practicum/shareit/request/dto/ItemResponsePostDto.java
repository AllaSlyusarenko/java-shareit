package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemResponsePostDto {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
