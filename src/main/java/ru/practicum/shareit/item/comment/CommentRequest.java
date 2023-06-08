package ru.practicum.shareit.item.comment;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommentRequest {
    @NotBlank
    private String text;
}