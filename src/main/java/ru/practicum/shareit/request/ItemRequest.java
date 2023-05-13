package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
public class ItemRequest {
    @Positive
    private long id;
    @Size(max = 200)
    private String description;
    @NotNull
    private User requestor; // или DTO
    @FutureOrPresent
    private LocalDateTime created;
}
