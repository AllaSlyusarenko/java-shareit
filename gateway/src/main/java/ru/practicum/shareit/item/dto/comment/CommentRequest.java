package ru.practicum.shareit.item.dto.comment;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommentRequest {
    @NotBlank
    private String text;
}