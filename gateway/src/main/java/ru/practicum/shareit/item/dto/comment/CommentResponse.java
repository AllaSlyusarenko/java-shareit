package ru.practicum.shareit.item.dto.comment;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommentResponse {
    @NotNull
    private Long id;
    @NotNull
    private String text;
    @NotNull
    private String authorName;
    @NotNull
    private LocalDateTime created;
}