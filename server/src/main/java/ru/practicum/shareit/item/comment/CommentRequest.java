package ru.practicum.shareit.item.comment;

import lombok.*;
import ru.practicum.shareit.Generated;


@Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommentRequest {
    private String text;
}