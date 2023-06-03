package ru.practicum.shareit.booking;

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
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public BookingResponseDto saveBooking(Long userId, BookingRequestDto bookingRequestDto) {
        if (!isValidDate(bookingRequestDto.getStart(), bookingRequestDto.getEnd())) {
            throw new ValidationException("Дата окончания должна быть позже, чем дата начала, и дата начала не можнт быть в прошлом");
        }
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден");
        }
        Optional<Item> item = itemRepository.findById(bookingRequestDto.getItemId());
        if (!item.isPresent()) {
            throw new NotFoundException("Вещь с id" + bookingRequestDto.getItemId() + " не найдена");
        }
        if (!item.get().getAvailable()) {
            throw new ValidationException("Вещь с id " + item.get().getId() + " на данный момент недоступна");
        }
        if (userId == item.get().getOwner().getId()) {
            throw new ValidationException("Хозяин не может быть и заказчиком данной вещи одновременно");
        }
        Booking booking = BookingMapper.mapToBooking(bookingRequestDto, user.get(),item.get());
        //проверить статус
        booking.setStatus(Status.WAITING);
        //booking.setBooker(user.get());
        Booking bookingSave = bookingRepository.save(booking);
        return BookingMapper.mapToBookingResponseDto(bookingSave);
    }

    @Override
    @Transactional
    public BookingResponseDto approveBooking(Long userId, Long bookingId, String approved) {
        User user = userRepository.getReferenceById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден");
        }
        Booking booking = bookingRepository.getReferenceById(bookingId);
        if (booking == null) {
            throw new NotFoundException("Запрос с id" + bookingId + " не найден");
        }
        if (booking.getItem().getOwner().getId() != userId) {
            throw new ValidationException("Только владелец вещи может подтвержать запрос");
        }
//        if (!booking.getStatus().equals(Status.WAITING)) {
//            throw new ValidationException("Вещь должна быть в статусе - ожидания подтверждения");
//        }
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
    public BookingResponseDto infoBooking(Long userId, Long bookingId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent() || user.get() == null) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден");
        }
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (!booking.isPresent()) {
            throw new NotFoundException("Запрос с id" + bookingId + " не найден");
        }
        if (userId != booking.get().getBooker().getId() && userId != booking.get().getItem().getOwner().getId()) {
            throw new ValidationException("Информация о бронировании доступна только владельцу или автору бронирования");
        }

        return BookingMapper.mapToBookingResponseDto(booking.get());
    }

    @Override
    public List<BookingResponseDto> allBookingUser(Long userId, String state) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent() || user.get() == null) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден");
        }
        List<Booking> result = new ArrayList<>();
        switch (state) {
            case ("ALL"):
                result.addAll(bookingRepository.findAllByBookerOrderByStartDesc(user.get()));
                break;
            case ("CURRENT"):
                break;
            case ("PAST"):
                break;
            case ("FUTURE"):
                break;
            case ("WAITING"):
                break;
            case ("REJECTED"):
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }

        return BookingMapper.mapToBookingResponseDto(result);
    }

    @Override
    public List<BookingResponseDto> allBookingOwner(Long ownerId, String state) {
        Optional<User> user = userRepository.findById(ownerId);
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователь с id" + ownerId + " не найден");
        }
        List<Booking> result = new ArrayList<>();
        switch (state) {
            case ("ALL"):
                result.addAll(bookingRepository.findAllByBookerOrderByStartDesc(user.get()));
                break;
            case ("CURRENT"):
                break;
            case ("PAST"):
                break;
            case ("FUTURE"):
                break;
            case ("WAITING"):
                break;
            case ("REJECTED"):
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }

        return BookingMapper.mapToBookingResponseDto(result);
    }

    private boolean isValidDate(LocalDateTime start, LocalDateTime end) {
        boolean result = false;
        if (start.isAfter(LocalDateTime.now()) && start.isBefore(end)) {
            result = true;
        }
        return result;
    }
}
