package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingResponseDto saveBooking(Long userId, BookingRequestDto bookingRequestDto) {
        if (!isValidDate(bookingRequestDto.getStart(), bookingRequestDto.getEnd())) {
            throw new ValidationException("Дата окончания должна быть позже, чем дата начала, и дата начала не можнт быть в прошлом");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id" + userId + " не найден"));
        Item item = itemRepository.findById(bookingRequestDto.getItemId()).orElseThrow(() -> new NotFoundException("Вещь с id" + bookingRequestDto.getItemId() + " не найдена"));
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь с id " + item.getId() + " на данный момент недоступна");
        }
        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Хозяин не может быть и заказчиком данной вещи одновременно");
        }
        Booking booking = BookingMapper.mapToBooking(bookingRequestDto, user, item);
        booking.setStatus(Status.WAITING);
        Booking bookingSave = bookingRepository.save(booking);
        return BookingMapper.mapToBookingResponseDto(bookingSave);
    }

    @Override
    @Transactional
    public BookingResponseDto approveBooking(Long userId, Long bookingId, String approved) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id" + userId + " не найден"));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Запрос с id" + bookingId + " не найден"));
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Только владелец вещи может подтверждать запрос");
        }
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new ValidationException("Вещь должна быть в статусе - ожидания подтверждения");
        }
        if (approved.equals("true")) {
            booking.setStatus(Status.APPROVED);
        } else if (approved.equals("false")) {
            booking.setStatus(Status.REJECTED);
        } else {
            throw new ValidationException("Недопустимое значение, должно быть или true,  или false");
        }
        Booking bookingSave = bookingRepository.save(booking);
        return BookingMapper.mapToBookingResponseDto(bookingSave);
    }

    @Override
    public BookingResponseDto infoBooking(Long userId, Long bookingId) { // кто просматривает - или автор брони, или владелец вещи
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id" + userId + " не найден"));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Запрос с id" + bookingId + " не найден"));
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Информация о бронировании доступна только владельцу или автору бронирования");
        }
        return BookingMapper.mapToBookingResponseDto(booking);
    }

    @Override
    public List<BookingResponseDto> allBookingUser(Long userId, String state) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id" + userId + " не найден"));
        LocalDateTime nowS = LocalDateTime.now();
        LocalDateTime nowE = LocalDateTime.now();
        List<Booking> result = new ArrayList<>();
        switch (state) {
            case ("ALL"):
                result.addAll(bookingRepository.findAllByBookerOrderByStartDesc(user));
                break;
            case ("CURRENT"):
                result.addAll(bookingRepository.findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStart(user, nowS, nowE));
                break;
            case ("PAST"):
                result.addAll(bookingRepository.findAllByBookerAndEndIsBeforeOrderByStartDesc(user, nowE));
                break;
            case ("FUTURE"):
                result.addAll(bookingRepository.findAllByBookerAndStartIsAfterOrderByStartDesc(user, nowS));
                break;
            case ("WAITING"):
                Status statusW = Status.WAITING;
                result.addAll(bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(user, statusW));
                break;
            case ("REJECTED"):
                Status statusR = Status.REJECTED;
                result.addAll(bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(user, statusR));
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }
        return BookingMapper.mapToBookingResponseDto(result);
    }

    @Override
    public List<BookingResponseDto> allBookingOwner(Long ownerId, String state) {
        User user = userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException("Пользователь с id" + ownerId + " не найден"));
        LocalDateTime nowS = LocalDateTime.now();
        LocalDateTime nowE = LocalDateTime.now();
        List<Booking> result = new ArrayList<>();
        switch (state) {
            case ("ALL"):
                result.addAll(bookingRepository.findAllByItem_OwnerOrderByStartDesc(user));
                break;
            case ("CURRENT"):
                result.addAll(bookingRepository.findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStart(user, nowS, nowE));
                break;
            case ("PAST"):
                result.addAll(bookingRepository.findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(user, nowE));
                break;
            case ("FUTURE"):
                result.addAll(bookingRepository.findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(user, nowS));
                break;
            case ("WAITING"):
                Status statusW = Status.WAITING;
                result.addAll(bookingRepository.findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(user, statusW));
                break;
            case ("REJECTED"):
                Status statusR = Status.REJECTED;
                result.addAll(bookingRepository.findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(user, statusR));
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }
        return BookingMapper.mapToBookingResponseDto(result);
    }

    private boolean isValidDate(LocalDateTime start, LocalDateTime end) {
        return start.isAfter(LocalDateTime.now()) && start.isBefore(end);
    }
}
