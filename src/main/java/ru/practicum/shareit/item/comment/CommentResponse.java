package ru.practicum.shareit.item.comment;

import lombok.*;
import ru.practicum.shareit.item.Item;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommentResponse {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
