package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.Generated;

import java.time.LocalDateTime;

@Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RequestResponsePostDto {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
