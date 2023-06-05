package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ItemDto saveItem(Long userId, ItemDto itemDto) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден");
        }
        Item item = ItemMapper.dtoToItem(user.get(), itemDto);
        Item itemSave = itemRepository.save(item);
        return ItemMapper.itemToDto(itemSave);
    }

    @Override
    public ItemShort findItemById(Long userId, Long id) {
        LocalDateTime now = LocalDateTime.now();
        Optional<Item> item = itemRepository.findById(id);
        if (!item.isPresent()) {
            throw new NotFoundException("Вещь с id" + id + " не найдена");
        }
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден");
        }
        List<Comment> comments = commentRepository.findAllByItem(item.get());
        if (item.get().getOwner().getId() == userId) { // просмотр вещи собственником
            Booking lastBookingFull = bookingRepository.findFirstByItemAndEndIsBeforeOrderByEndDesc(item.get(), now);
            if (lastBookingFull != null && lastBookingFull.getStatus() == Status.REJECTED) {
                lastBookingFull = null;
            }
            Booking nextBookingFull = bookingRepository.findFirstByItemAndStartIsAfterOrderByStart(item.get(), now);
            if (nextBookingFull != null && nextBookingFull.getStatus() == Status.REJECTED) {
                nextBookingFull = null;
            }
            BookingShort lastBooking = BookingMapper.mapToBookingShort(lastBookingFull);
            BookingShort nextBooking = BookingMapper.mapToBookingShort(nextBookingFull);
            return ItemMapper.itemShortDto(item.get(), lastBooking, nextBooking, comments);
        }
        return ItemMapper.itemShortDto(item.get(), null, null, comments); // просмотр вещи всеми остальными
    }

    @Override
    public List<ItemShort> findAllUserItems(Long userId) { // владелец вещи
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден");
        }
        LocalDateTime now = LocalDateTime.now();
        List<ItemShort> itemsOwner = new ArrayList<>();
        List<Item> items = itemRepository.findAllByOwner(user.get());
        for (Item item : items) {
            Booking lastBookingFull = bookingRepository.findFirstByItemAndEndIsBeforeOrderByEndDesc(item, now);
            if (lastBookingFull != null && lastBookingFull.getStatus() == Status.REJECTED) {
                lastBookingFull = null;
            }
            Booking nextBookingFull = bookingRepository.findFirstByItemAndStartIsAfterOrderByStart(item, now);
            if (nextBookingFull != null && nextBookingFull.getStatus() == Status.REJECTED) {
                nextBookingFull = null;
            }
            BookingShort lastBooking = BookingMapper.mapToBookingShort(lastBookingFull);
            BookingShort nextBooking = BookingMapper.mapToBookingShort(nextBookingFull);
            List<Comment> comments = commentRepository.findAllByItem(item);
            ItemShort dtoToAdd = ItemMapper.itemShortDto(item, lastBooking, nextBooking, comments);
            itemsOwner.add(dtoToAdd);
        }
        return itemsOwner;

    }

    @Override
    public ItemDto updateItem(Long userId, Long id, ItemDto itemDto) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден");
        }
        Optional<Item> item = itemRepository.findById(id);
        if (!item.isPresent()) {
            throw new NotFoundException("Вещь с id" + id + " не найдена");
        }
        if (itemDto.getName() != null) {
            item.get().setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.get().setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.get().setAvailable(itemDto.getAvailable());
        }
        item.get().setOwner(user.get());
        Item itemSave = itemRepository.save(item.get());
        return ItemMapper.itemToDto(itemSave);
    }

    @Override
    public List<ItemDto> findItemByNameOrDescription(Long userId, String text) {
        if (text.isBlank()) {
            return ItemMapper.itemsToDto(new ArrayList<>());
        }
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден");
        }
        List<Item> items = itemRepository.search(text);
        return ItemMapper.itemsToDto(items);
    }

    @Override
    public void deleteItemById(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public CommentResponse saveComment(Long userId, Long id, CommentRequest commentRequest) {
        LocalDateTime now = LocalDateTime.now();
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден");
        }
        Optional<Item> item = itemRepository.findById(id);
        if (!item.isPresent()) {
            throw new NotFoundException("Вещь с id" + id + " не найдена");
        }
        Booking booking =
                bookingRepository.findFirstByBookerAndItemAndEndIsBeforeOrderByEndDesc(
                        user.get(), item.get(), now);
        if (booking == null) {
            throw new ValidationException("Пользователь c id" + userId + " не использовал вещь c id" + id);
        }
        Comment comment = CommentMapper.mapToComment(commentRequest, user.get(), item.get(), now);
        Comment commentSave = commentRepository.save(comment);
        return CommentMapper.mapToCommentResponse(commentSave);
    }
}
