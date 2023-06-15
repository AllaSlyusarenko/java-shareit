package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public ItemDto saveItem(Long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id" + userId + " не найден"));
        Request request = null;
        if (itemDto.getRequestId() != null) {
            request = requestRepository.findById(itemDto.getRequestId()).orElseThrow(() -> new NotFoundException("Запрос не найден"));
        }
        Item item = ItemMapper.dtoToItem(user, itemDto, request);
        Item itemSave = itemRepository.save(item);
        return ItemMapper.itemToDto(itemSave);
    }

    @Override
    @Transactional
    public ItemShort findItemById(Long userId, Long id) {
        LocalDateTime now = LocalDateTime.now();
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Вещь с id" + id + " не найдена"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id" + userId + " не найден"));
        List<Comment> comments = commentRepository.findAllByItem(item);
        List<CommentResponse> commentsResponse = CommentMapper.mapToCommentResponseList(comments);
        if (item.getOwner().getId().equals(userId)) { // просмотр вещи собственником
            Booking lastBookingFull = bookingRepository.findFirstByItemAndStartIsBeforeOrStartEqualsOrderByStartDesc(item, now, now);
            if (lastBookingFull != null && lastBookingFull.getStatus() == Status.REJECTED) {
                lastBookingFull = null;
            }
            Booking nextBookingFull = bookingRepository.findFirstByItemAndStartIsAfterOrderByStart(item, now);
            if (nextBookingFull != null && nextBookingFull.getStatus() == Status.REJECTED) {
                nextBookingFull = null;
            }
            BookingShort lastBooking = BookingMapper.mapToBookingShort(lastBookingFull);
            BookingShort nextBooking = BookingMapper.mapToBookingShort(nextBookingFull);
            return ItemMapper.itemShortDto(item, lastBooking, nextBooking, commentsResponse);
        }
        return ItemMapper.itemShortDto(item, null, null, commentsResponse); // просмотр вещи всеми остальными
    }

    @Override
    public List<ItemShort> findAllUserItems(Long userId, Integer from, Integer size) { // владелец вещи
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id" + userId + " не найден"));
        if (from < 0 || size <= 0) {
            throw new ValidationException("from должно быть неотрицательное и size положительное");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        LocalDateTime now = LocalDateTime.now();
        List<ItemShort> itemsOwner = new ArrayList<>();
        List<Item> items = itemRepository.findAllByOwner(user, pageable);
        for (Item item : items) {
            Booking lastBookingFull = bookingRepository.findFirstByItemAndStartIsBeforeOrStartEqualsOrderByStartDesc(item, now, now);
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
            List<CommentResponse> commentsResponse = CommentMapper.mapToCommentResponseList(comments);
            ItemShort dtoToAdd = ItemMapper.itemShortDto(item, lastBooking, nextBooking, commentsResponse);
            itemsOwner.add(dtoToAdd);
        }
        return itemsOwner;
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long userId, Long id, ItemDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id" + userId + " не найден"));
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Вещь с id" + id + " не найдена"));
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        item.setOwner(user);
        Item itemSave = itemRepository.save(item);
        return ItemMapper.itemToDto(itemSave);
    }

    @Override
    public List<ItemDto> findItemByNameOrDescription(Long userId, String text, Integer from, Integer size) {
        if (text.isBlank()) {
            return ItemMapper.itemsToDto(new ArrayList<>());
        }
        if (from < 0 || size <= 0) {
            throw new ValidationException("from должно быть неотрицательное и size положительное");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id" + userId + " не найден"));
        List<Item> items = itemRepository.search(text, pageable);
        return ItemMapper.itemsToDto(items);
    }

    @Override
    public void deleteItemById(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public CommentResponse saveComment(Long userId, Long id, CommentRequest commentRequest) {
        LocalDateTime now = LocalDateTime.now();
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id" + userId + " не найден"));
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Вещь с id" + id + " не найдена"));
        Booking booking =
                bookingRepository.findFirstByBookerAndItemAndEndIsBeforeOrderByEndDesc(user, item, now);
        if (booking == null) {
            throw new ValidationException("Пользователь c id" + userId + " не использовал вещь c id" + id);
        }
        Comment comment = CommentMapper.mapToComment(commentRequest, user, item, now);
        Comment commentSave = commentRepository.save(comment);
        return CommentMapper.mapToCommentResponse(commentSave);
    }
}