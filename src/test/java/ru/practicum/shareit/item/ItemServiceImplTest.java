package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.CommentRequest;
import ru.practicum.shareit.item.comment.CommentResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    private ItemService itemService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private RequestRepository requestRepository;

    @BeforeEach
    public void setUp() {
        itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository, requestRepository);
    }

    @Test
    @DirtiesContext
    void saveItem_shouldNotSaveItem_whenUserNotFound() {
        //given
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.saveItem(1L, Mockito.any(ItemDto.class)));
        verify(itemRepository, never()).save(Mockito.any(Item.class));
        assertThat(exception.getMessage(), containsString("Пользователь с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void saveItem_shouldNotSaveItem_whenRequestNotFound() {
        //given
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        ItemDto itemDto = ItemDto.builder().name("item").description("item").available(true).requestId(1L).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.saveItem(1L, itemDto));
        verify(itemRepository, never()).save(Mockito.any(Item.class));
        assertThat(exception.getMessage(), containsString("Запрос не найден"));
    }

    @Test
    @DirtiesContext
    void saveItem_shouldSaveItem_whenDataIsCorrect() {
        //given
        User requestor = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User owner = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        ItemDto itemDtoIn = ItemDto.builder().name("item").description("item").available(true).requestId(1L).build();
        LocalDateTime created = LocalDateTime.now();
        Request request = Request.builder().id(1L).description("request").requestor(requestor).created(created).build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).request(request).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(requestor));
        when(requestRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(request));
        when(itemRepository.save(Mockito.any(Item.class))).thenReturn(item);
        ItemDto itemDtoOut = itemService.saveItem(1L, itemDtoIn);
        //then
        assertThat(itemDtoOut, instanceOf(ItemDto.class));
        assertEquals(itemDtoOut.getName(), itemDtoIn.getName());
        assertEquals(itemDtoOut.getDescription(), itemDtoIn.getDescription());
        assertEquals(itemDtoOut.getAvailable(), itemDtoIn.getAvailable());
        assertEquals(itemDtoOut.getRequestId(), itemDtoIn.getRequestId());
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(requestRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).save(Mockito.any());
        verifyNoMoreInteractions(itemRepository, userRepository, requestRepository);
    }

    @Test
    @DirtiesContext
    void findItemById_shouldNotGetItem_whenItemNotFound() {
        //given
        //when
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.findItemById(1L, 1L));
        assertThat(exception.getMessage(), containsString("Вещь с id 1 не найдена"));
    }

    @Test
    @DirtiesContext
    void findItemById_shouldNotGetItem_whenUserNotFound() {
        //given
        Item item = Item.builder().id(1L).name("item").description("item").available(true).build();
        //when
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.findItemById(1L, Mockito.anyLong()));
        assertThat(exception.getMessage(), containsString("Пользователь с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void findItemById_shouldGetItem_whenDataIsCorrect() {
        //given
        User booker = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User owner = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        LocalDateTime now = LocalDateTime.now();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        List<Comment> comments = new ArrayList<>();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = now.plusDays(1);
        Booking lastBookingFull = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker).status(Status.APPROVED).build();
        Booking nextBookingFull = null;
        //when
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(owner));
        when(commentRepository.findAllByItem(Mockito.any(Item.class))).thenReturn(comments);
        when(bookingRepository.findFirstByItemAndStartIsBeforeOrStartEqualsOrderByStartDesc(Mockito.any(Item.class),
                Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class))).thenReturn(lastBookingFull);
        when(bookingRepository.findFirstByItemAndStartIsAfterOrderByStart(Mockito.any(Item.class),
                Mockito.any(LocalDateTime.class))).thenReturn(nextBookingFull);
        ItemShort itemShortOwner = itemService.findItemById(2L, 1L);
        //then
        assertThat(itemShortOwner, instanceOf(ItemShort.class));
        assertEquals(itemShortOwner.getName(), item.getName());
        assertEquals(itemShortOwner.getDescription(), item.getDescription());
        assertEquals(itemShortOwner.getAvailable(), item.getAvailable());
        assertThat(itemShortOwner.getLastBooking(), instanceOf(BookingShort.class));
        assertNull(itemShortOwner.getNextBooking());
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(commentRepository, times(1)).findAllByItem(Mockito.any(Item.class));
        verify(itemRepository, times(1)).findById(Mockito.anyLong());
        verifyNoMoreInteractions(itemRepository, userRepository, commentRepository, bookingRepository);
    }

    @Test
    @DirtiesContext
    void findAllUserItems_shouldNotGetItem_whenUserNotFound() {
        //given
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.findAllUserItems(1L, 0, 2));
        verify(itemRepository, never()).findAllByOwner(Mockito.any(User.class), Mockito.any(Pageable.class));
        assertThat(exception.getMessage(), containsString("Пользователь с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void findAllUserItems_shouldGetItem_whenDataIsCorrect() {
        //given
        User booker = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User owner = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        LocalDateTime now = LocalDateTime.now();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        List<Comment> comments = new ArrayList<>();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = now.plusDays(1);
        Booking lastBookingFull = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker).status(Status.APPROVED).build();
        Booking nextBookingFull = null;
        Integer from = 0;
        Integer size = 2;
        //when
        when(itemRepository.findAllByOwner(Mockito.any(User.class), Mockito.any(Pageable.class))).thenReturn(List.of(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(owner));
        when(commentRepository.findAllByItem(Mockito.any(Item.class))).thenReturn(comments);
        when(bookingRepository.findFirstByItemAndStartIsBeforeOrStartEqualsOrderByStartDesc(Mockito.any(Item.class),
                Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class))).thenReturn(lastBookingFull);
        when(bookingRepository.findFirstByItemAndStartIsAfterOrderByStart(Mockito.any(Item.class),
                Mockito.any(LocalDateTime.class))).thenReturn(nextBookingFull);
        List<ItemShort> itemShortOwners = itemService.findAllUserItems(2L, from, size);
        //then
        assertFalse(itemShortOwners.isEmpty());
        assertEquals(itemShortOwners.get(0).getName(), item.getName());
        assertEquals(itemShortOwners.get(0).getDescription(), item.getDescription());
        assertEquals(itemShortOwners.get(0).getAvailable(), item.getAvailable());
        assertThat(itemShortOwners.get(0).getLastBooking(), instanceOf(BookingShort.class));
        assertNull(itemShortOwners.get(0).getNextBooking());
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(commentRepository, times(1)).findAllByItem(Mockito.any(Item.class));
        verify(itemRepository, times(1)).findAllByOwner(Mockito.any(User.class), Mockito.any(Pageable.class));
        verify(bookingRepository, times(1)).findFirstByItemAndStartIsBeforeOrStartEqualsOrderByStartDesc(
                Mockito.any(Item.class), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class));
        verify(bookingRepository, times(1)).findFirstByItemAndStartIsAfterOrderByStart(Mockito.any(Item.class), Mockito.any(LocalDateTime.class));
        verifyNoMoreInteractions(itemRepository, userRepository, commentRepository, bookingRepository);
    }

    @Test
    @DirtiesContext
    void updateItem_shouldNotUpdateItem_whenUserNotFound() {
        //given
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.updateItem(1L, 1L, Mockito.any(ItemDto.class)));
        verify(itemRepository, never()).save(Mockito.any(Item.class));
        assertThat(exception.getMessage(), containsString("Пользователь с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void updateItem_shouldNotUpdateItem_whenItemNotFound() {
        //given
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.updateItem(1L, 1L, Mockito.any(ItemDto.class)));
        verify(itemRepository, never()).save(Mockito.any(Item.class));
        assertThat(exception.getMessage(), containsString("Вещь с id 1 не найдена"));
    }

    @Test
    @DirtiesContext
    void updateItem_shouldUpdateItem_whenDataIsCorrect() {
        //given
        User requestor = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User user = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        ItemDto itemDtoIn = ItemDto.builder().id(1L).name("item1").description("item1").available(true).requestId(1L).build();
        LocalDateTime created = LocalDateTime.now();
        Request request = Request.builder().id(1L).description("request").requestor(requestor).created(created).build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(user).request(request).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(Mockito.any(Item.class))).thenReturn(item);
        ItemDto itemDtoOut = itemService.updateItem(1L, 1L, itemDtoIn);
        //then
        assertThat(itemDtoOut, instanceOf(ItemDto.class));
        assertEquals(itemDtoOut.getName(), itemDtoIn.getName());
        assertEquals(itemDtoOut.getDescription(), itemDtoIn.getDescription());
        assertEquals(itemDtoOut.getAvailable(), itemDtoIn.getAvailable());
        assertEquals(itemDtoOut.getRequestId(), itemDtoIn.getRequestId());
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).save(Mockito.any());
        verifyNoMoreInteractions(itemRepository, userRepository);
    }

    @Test
    @DirtiesContext
    void findItemByNameOrDescription_shouldNotGetItem_whenUserNotFound() {
        //given
        Integer from = 0;
        Integer size = 2;
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.findItemByNameOrDescription(1L, "ALL", from, size));
        verify(itemRepository, never()).search(Mockito.anyString(), Mockito.any(Pageable.class));
        assertThat(exception.getMessage(), containsString("Пользователь с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void findItemByNameOrDescription_shouldGetItem_whenDataIsCorrect() {
        //given
        User booker = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User owner = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        Integer from = 0;
        Integer size = 2;
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.search(Mockito.anyString(), Mockito.any(Pageable.class))).thenReturn(List.of(item));
        List<ItemDto> itemDtos = itemService.findItemByNameOrDescription(1L, "qwerty", from, size);
        //then
        assertFalse(itemDtos.isEmpty());
        assertEquals(itemDtos.get(0).getName(), item.getName());
        assertEquals(itemDtos.get(0).getDescription(), item.getDescription());
        assertEquals(itemDtos.get(0).getAvailable(), item.getAvailable());
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).search(Mockito.anyString(), Mockito.any(Pageable.class));
        verifyNoMoreInteractions(itemRepository, userRepository);
    }

    @Test
    @DirtiesContext
    void saveComment_shouldNotSave_whenUserNotFound() {
        //given
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.saveComment(1L, 1L, Mockito.any(CommentRequest.class)));
        verify(commentRepository, never()).save(Mockito.any(Comment.class));
        assertThat(exception.getMessage(), containsString("Пользователь с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void saveComment_shouldNotSave_whenItemNotFound() {
        //given
        User booker = User.builder().id(1L).name("name").email("name@ya.ru").build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.saveComment(1L, 1L, Mockito.any(CommentRequest.class)));
        verify(commentRepository, never()).save(Mockito.any(Comment.class));
        assertThat(exception.getMessage(), containsString("Вещь с id 1 не найдена"));
    }

    @Test
    @DirtiesContext
    void saveComment_shouldNotSave_whenBookingNotFound() {
        //given
        User booker = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User owner = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByBookerAndItemAndEndIsBeforeOrderByEndDesc(
                Mockito.any(User.class), Mockito.any(Item.class), Mockito.any(LocalDateTime.class))).thenReturn(null);
        //then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> itemService.saveComment(1L, 1L, Mockito.any(CommentRequest.class)));
        verify(commentRepository, never()).save(Mockito.any(Comment.class));
        assertThat(exception.getMessage(), containsString("Пользователь c id 1 не использовал вещь c id 1"));
    }

    @Test
    @DirtiesContext
    void saveComment_shouldSave_whenDataIsCorrect() {
        //given
        User booker = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User owner = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        LocalDateTime now = LocalDateTime.now();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = now.plusDays(1);
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker).status(Status.APPROVED).build();
        CommentRequest commentRequest = CommentRequest.builder().text("comment").build();
        Comment comment = Comment.builder().id(1L).text("comment").item(item).author(booker).created(now).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByBookerAndItemAndEndIsBeforeOrderByEndDesc(
                Mockito.any(User.class), Mockito.any(Item.class), Mockito.any(LocalDateTime.class))).thenReturn(booking);
        when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);
        CommentResponse commentResponse = itemService.saveComment(1L, 1L, commentRequest);
        //then
        assertThat(commentResponse, instanceOf(CommentResponse.class));
        assertEquals(commentResponse.getText(), commentRequest.getText());
        assertEquals(commentResponse.getAuthorName(), booker.getName());
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).findById(Mockito.anyLong());
        verify(commentRepository, times(1)).save(Mockito.any(Comment.class));
        verifyNoMoreInteractions(itemRepository, userRepository, commentRepository, bookingRepository);
    }
}