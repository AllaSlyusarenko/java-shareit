package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Optional;


@Service
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
    public BookingDto saveBooking(Long userId, BookingDto bookingDto) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден");
        }
        Optional<Item> item = itemRepository.findById(bookingDto.getItem().getId());
        if (!item.isPresent()) {
            throw new NotFoundException("Вещь с id" + bookingDto.getItem().getId() + " не найдена");
        }
        if (!item.get().getAvailable()) {
            throw new ValidationException("Вещь с id " + item.get().getId() + " на данный момент недоступна");
        }
        Booking booking = BookingMapper.mapToBooking(bookingDto);
        booking.setStatus(Status.WAITING);
        booking.setBooker(user.get());
        booking = bookingRepository.save(booking);
        return BookingMapper.mapToBookingDto(booking);
    }
}
