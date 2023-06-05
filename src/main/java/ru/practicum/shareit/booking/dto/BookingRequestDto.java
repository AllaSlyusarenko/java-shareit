package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookingRequestDto {

    @NotNull
    @JsonProperty("itemId")
    private Long itemId;

    @NotNull
    @JsonProperty("start")
    private LocalDateTime start;

    @NotNull
    @JsonProperty("end")
    private LocalDateTime end;
}