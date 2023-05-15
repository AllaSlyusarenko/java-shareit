package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequest {
    @Positive
    private long id;
    @Size(max = 200)
    private String description;
    @NotNull
    private User requestor;
    @FutureOrPresent
    private LocalDateTime created;
}
