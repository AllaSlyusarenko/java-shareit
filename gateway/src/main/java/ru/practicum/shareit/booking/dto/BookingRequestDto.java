package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
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
    @FutureOrPresent
    @JsonProperty("start")
    private LocalDateTime start;

    @NotNull
    @Future
    @JsonProperty("end")
    private LocalDateTime end;
}