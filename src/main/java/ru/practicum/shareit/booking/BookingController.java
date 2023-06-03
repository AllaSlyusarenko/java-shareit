package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.NewBooking;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto saveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Создание новой вещи");
        return bookingService.saveBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable @Positive Long bookingId,
                                             @RequestParam String approved) {
        log.info("Подтверждение или отклонение запроса на бронирование");
        BookingResponseDto bookingResponseDto = bookingService.approveBooking(userId, bookingId, approved);
        return bookingResponseDto;
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable @Positive Long bookingId) {
        log.info("Подтверждение или отклонение запроса на бронирование");
        return bookingService.infoBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> allBookingUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "ALL", required = false) String state) {
        log.info("Получение списка всех бронирований пользователя");
        return bookingService.allBookingUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> allBookingOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                   @RequestParam(defaultValue = "ALL", required = false) String state) {
        log.info("Получение списка всех бронирований владельца");
        return bookingService.allBookingOwner(ownerId, state);
    }

}
