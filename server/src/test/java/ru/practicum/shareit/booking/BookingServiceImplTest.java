package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    private BookingService bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Captor
    private ArgumentCaptor<Booking> bookingArgumentCaptor;

    @BeforeEach
    public void setUp() {
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
    }

    @Test
    @DirtiesContext
    void saveBooking_shouldNotSaveBooking_whenUserNotFound() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder().itemId(1L).start(start).end(end).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.saveBooking(1L, bookingRequestDto));
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
        assertThat(exception.getMessage(), containsString("Пользователь с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void saveBooking_shouldNotSaveBooking_whenWrongValidationDate() {
        //given
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.minusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder().itemId(1L).start(start).end(end).build();
        //when
        //then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.saveBooking(1L, bookingRequestDto));
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
        assertThat(exception.getMessage(), containsString("Дата окончания должна быть позже, чем дата начала, и дата начала не может быть в прошлом"));
    }

    @Test
    @DirtiesContext
    void saveBooking_shouldNotSaveBooking_whenItemNotFound() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(2L).name("name").email("name@ya.ru").build();
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder().itemId(1L).start(start).end(end).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.saveBooking(1L, bookingRequestDto));
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
        assertThat(exception.getMessage(), containsString("Вещь с id 1 не найдена"));
    }

    @Test
    @DirtiesContext
    void saveBooking_shouldNotSaveBooking_whenItemIsNotAvailable() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(false).owner(user).build();
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder().itemId(1L).start(start).end(end).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        //then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.saveBooking(1L, bookingRequestDto));
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
        assertThat(exception.getMessage(), containsString("Вещь с id 1 на данный момент недоступна"));
    }

    @Test
    @DirtiesContext
    void saveBooking_shouldNotSaveBooking_whenOwnerIsBooker() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(user).build();
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder().itemId(1L).start(start).end(end).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.saveBooking(1L, bookingRequestDto));
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
        assertThat(exception.getMessage(), containsString("Хозяин не может быть и заказчиком данной вещи одновременно"));
    }

    @Test
    @DirtiesContext
    void saveBooking_shouldSaveBooking_whenDataCorrect() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User owner = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder().itemId(1L).start(start).end(end).build();
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(user).status(Status.WAITING).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(Mockito.any())).thenReturn(booking);
        BookingResponseDto bookingResponseDto = bookingService.saveBooking(1L, bookingRequestDto);
        //then
        assertThat(bookingResponseDto, instanceOf(BookingResponseDto.class));
        assertThat(bookingResponseDto.getItem(), instanceOf(Item.class));
        assertThat(bookingResponseDto.getBooker(), instanceOf(User.class));
        assertEquals(bookingResponseDto.getStatus(), Status.WAITING);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).save(Mockito.any());
        verifyNoMoreInteractions(itemRepository, userRepository, bookingRepository);
    }

    @Test
    @DirtiesContext
    void approveBooking_shouldNotApproveBooking_whenUserNotFound() {
        //given
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.approveBooking(1L, Mockito.anyLong(), "true"));
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
        assertThat(exception.getMessage(), containsString("Пользователь с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void approveBooking_shouldNotApproveBooking_whenBookingNotFound() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder().itemId(1L).start(start).end(end).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.approveBooking(1L, 1L, "true"));
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
        assertThat(exception.getMessage(), containsString("Запрос с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void approveBooking_shouldNotApproveBooking_whenUserIsNotOwner() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(user).build();
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker).status(Status.WAITING).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.approveBooking(2L, 1L, "true"));
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
        assertThat(exception.getMessage(), containsString("Только владелец вещи может подтверждать запрос"));
    }

    @Test
    @DirtiesContext
    void approveBooking_shouldNotApproveBooking_whenStatuIsNotWAITING() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(user).build();
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker).status(Status.REJECTED).build();
        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        //then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.approveBooking(1L, 1L, "true"));
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
        assertThat(exception.getMessage(), containsString("Вещь должна быть в статусе - ожидания подтверждения"));
    }

    @Test
    @DirtiesContext
    void approveBooking_shouldNotApproveBooking_whenStatuIsUnknown() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(user).build();
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker).status(Status.WAITING).build();
        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        //then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.approveBooking(1L, 1L, "qwerty"));
        verify(bookingRepository, never()).save(Mockito.any(Booking.class));
        assertThat(exception.getMessage(), containsString("Недопустимое значение, должно быть или true,  или false"));
    }

    @Test
    @DirtiesContext
    void approveBooking_shouldNotApproveBooking_whenDataIsOkTrue() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(user).build();
        Booking bookingIn = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker).status(Status.WAITING).build();
        Booking bookingOut = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker).status(Status.APPROVED).build();
        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingIn));
        when(bookingRepository.save(bookingIn)).thenReturn(bookingOut);
        BookingResponseDto bookingResponseDto = bookingService.approveBooking(1L, 1L, "true");
        //then
        assertThat(bookingResponseDto, instanceOf(BookingResponseDto.class));
        assertThat(bookingResponseDto.getItem(), instanceOf(Item.class));
        assertThat(bookingResponseDto.getBooker(), instanceOf(User.class));
        assertEquals(bookingResponseDto.getStatus(), Status.APPROVED);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).save(Mockito.any());
        verifyNoMoreInteractions(itemRepository, userRepository, bookingRepository);
    }

    @Test
    @DirtiesContext
    void approveBooking_shouldNotApproveBooking_whenDataIsOkFalse() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(user).build();
        Booking bookingIn = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker).status(Status.WAITING).build();
        Booking bookingOut = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker).status(Status.REJECTED).build();
        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingIn));
        when(bookingRepository.save(bookingIn)).thenReturn(bookingOut);
        BookingResponseDto bookingResponseDto = bookingService.approveBooking(1L, 1L, "false");
        //then
        assertThat(bookingResponseDto, instanceOf(BookingResponseDto.class));
        assertThat(bookingResponseDto.getItem(), instanceOf(Item.class));
        assertThat(bookingResponseDto.getBooker(), instanceOf(User.class));
        assertEquals(bookingResponseDto.getStatus(), Status.REJECTED);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).save(Mockito.any());
        verifyNoMoreInteractions(itemRepository, userRepository, bookingRepository);
    }

    @Test
    @DirtiesContext
    void infoBooking_shouldNotSaveBooking_whenUserNotFound() {
        //given
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.infoBooking(1L, Mockito.anyLong()));
        verify(bookingRepository, never()).findById(Mockito.anyLong());
        assertThat(exception.getMessage(), containsString("Пользователь с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void infoBooking_shouldNotApproveBooking_whenUserIsNotCorrect() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User owner = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        User viewer = User.builder().id(3L).name("name3").email("name3@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker).status(Status.APPROVED).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(viewer));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.infoBooking(3L, 1L));
        verify(bookingRepository, times(1)).findById(1L);
        assertThat(exception.getMessage(), containsString("Информация о бронировании доступна только владельцу или автору бронирования"));
    }

    @Test
    @DirtiesContext
    void infoBooking_shouldNotApproveBooking_whenDataIsCorrect() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User owner = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker).status(Status.APPROVED).build();
        //when
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        BookingResponseDto bookingResponseDto = bookingService.infoBooking(1L, 1L);
        //then
        assertThat(bookingResponseDto, instanceOf(BookingResponseDto.class));
        assertThat(bookingResponseDto.getItem(), instanceOf(Item.class));
        assertThat(bookingResponseDto.getBooker(), instanceOf(User.class));
        assertEquals(bookingResponseDto.getStatus(), Status.APPROVED);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findById(Mockito.anyLong());
        verifyNoMoreInteractions(userRepository, bookingRepository);
    }

    @Test
    @DirtiesContext
    void allBookingUser_shouldNotGetBooking_whenUserNotFound() {
        //given
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        Integer from = 0;
        Integer size = 2;
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("end").descending());
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.allBookingUser(1L, "ALL", from, size));
        verify(bookingRepository, never()).findAllByBookerOrderByStartDesc(user, pageable);
        assertThat(exception.getMessage(), containsString("Пользователь с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void allBookingUser_shouldGetBooking_whenDataIsCorrectALL() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        Item item = Item.builder().id(2L).name("item").description("item").available(true).build();
        Integer from = 1;
        Integer size = 2;
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(user).status(Status.WAITING).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerOrderByStartDesc(Mockito.any(User.class), Mockito.any(Pageable.class))).thenReturn(List.of(booking));
        List<BookingResponseDto> bookingResponseDtos = bookingService.allBookingUser(1L, "ALL", from, size);
        //then
        assertFalse(bookingResponseDtos.isEmpty());
        assertThat(bookingResponseDtos.get(0).getItem(), instanceOf(Item.class));
        assertThat(bookingResponseDtos.get(0).getBooker(), instanceOf(User.class));
        assertEquals(bookingResponseDtos.get(0).getStatus(), Status.WAITING);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findAllByBookerOrderByStartDesc(Mockito.any(User.class), Mockito.any(Pageable.class));
        verifyNoMoreInteractions(itemRepository, userRepository, bookingRepository);
    }

    @Test
    @DirtiesContext
    void allBookingUser_shouldGetBooking_whenDataIsCorrectCURRENT() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        Item item = Item.builder().id(2L).name("item").description("item").available(true).build();
        Integer from = 1;
        Integer size = 2;
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(user).status(Status.WAITING).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStart(
                Mockito.any(User.class), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponseDto> bookingResponseDtos = bookingService.allBookingUser(1L, "CURRENT", from, size);
        //then
        assertFalse(bookingResponseDtos.isEmpty());
        assertThat(bookingResponseDtos.get(0).getItem(), instanceOf(Item.class));
        assertThat(bookingResponseDtos.get(0).getBooker(), instanceOf(User.class));
        assertEquals(bookingResponseDtos.get(0).getStatus(), Status.WAITING);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStart(
                Mockito.any(User.class), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class));
        verifyNoMoreInteractions(itemRepository, userRepository, bookingRepository);
    }

    @Test
    @DirtiesContext
    void allBookingUser_shouldGetBooking_whenDataIsCorrectPAST() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        Item item = Item.builder().id(2L).name("item").description("item").available(true).build();
        Integer from = 1;
        Integer size = 2;
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(user).status(Status.WAITING).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerAndEndIsBeforeOrderByStartDesc(
                Mockito.any(User.class), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponseDto> bookingResponseDtos = bookingService.allBookingUser(1L, "PAST", from, size);
        //then
        assertFalse(bookingResponseDtos.isEmpty());
        assertThat(bookingResponseDtos.get(0).getItem(), instanceOf(Item.class));
        assertThat(bookingResponseDtos.get(0).getBooker(), instanceOf(User.class));
        assertEquals(bookingResponseDtos.get(0).getStatus(), Status.WAITING);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findAllByBookerAndEndIsBeforeOrderByStartDesc(
                Mockito.any(User.class), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class));
        verifyNoMoreInteractions(itemRepository, userRepository, bookingRepository);
    }

    @Test
    @DirtiesContext
    void allBookingUser_shouldGetBooking_whenDataIsCorrectFUTURE() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        Item item = Item.builder().id(2L).name("item").description("item").available(true).build();
        Integer from = 1;
        Integer size = 2;
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(user).status(Status.WAITING).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerAndStartIsAfterOrderByStartDesc(
                Mockito.any(User.class), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponseDto> bookingResponseDtos = bookingService.allBookingUser(1L, "FUTURE", from, size);
        //then
        assertFalse(bookingResponseDtos.isEmpty());
        assertThat(bookingResponseDtos.get(0).getItem(), instanceOf(Item.class));
        assertThat(bookingResponseDtos.get(0).getBooker(), instanceOf(User.class));
        assertEquals(bookingResponseDtos.get(0).getStatus(), Status.WAITING);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findAllByBookerAndStartIsAfterOrderByStartDesc(
                Mockito.any(User.class), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class));
        verifyNoMoreInteractions(itemRepository, userRepository, bookingRepository);
    }

    @Test
    @DirtiesContext
    void allBookingUser_shouldGetBooking_whenDataIsCorrectWAITING() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        Item item = Item.builder().id(2L).name("item").description("item").available(true).build();
        Integer from = 1;
        Integer size = 2;
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(user).status(Status.WAITING).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(
                Mockito.any(User.class), Mockito.any(Status.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponseDto> bookingResponseDtos = bookingService.allBookingUser(1L, "WAITING", from, size);
        //then
        assertFalse(bookingResponseDtos.isEmpty());
        assertThat(bookingResponseDtos.get(0).getItem(), instanceOf(Item.class));
        assertThat(bookingResponseDtos.get(0).getBooker(), instanceOf(User.class));
        assertEquals(bookingResponseDtos.get(0).getStatus(), Status.WAITING);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findAllByBookerAndStatusEqualsOrderByStartDesc(
                Mockito.any(User.class), Mockito.any(Status.class), Mockito.any(Pageable.class));
        verifyNoMoreInteractions(itemRepository, userRepository, bookingRepository);
    }

    @Test
    @DirtiesContext
    void allBookingUser_shouldGetBooking_whenDataIsCorrectREJECTED() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        Item item = Item.builder().id(2L).name("item").description("item").available(true).build();
        Integer from = 1;
        Integer size = 2;
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(user).status(Status.REJECTED).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(
                Mockito.any(User.class), Mockito.any(Status.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponseDto> bookingResponseDtos = bookingService.allBookingUser(1L, "REJECTED", from, size);
        //then
        assertFalse(bookingResponseDtos.isEmpty());
        assertThat(bookingResponseDtos.get(0).getItem(), instanceOf(Item.class));
        assertThat(bookingResponseDtos.get(0).getBooker(), instanceOf(User.class));
        assertEquals(bookingResponseDtos.get(0).getStatus(), Status.REJECTED);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findAllByBookerAndStatusEqualsOrderByStartDesc(
                Mockito.any(User.class), Mockito.any(Status.class), Mockito.any(Pageable.class));
        verifyNoMoreInteractions(itemRepository, userRepository, bookingRepository);
    }

    @Test
    @DirtiesContext
    void allBookingUser_shouldGetBooking_whenDataIsCorrectUnknown() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        Item item = Item.builder().id(2L).name("item").description("item").available(true).build();
        Integer from = 1;
        Integer size = 2;
        String state = "Qwerty";
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(user).status(Status.REJECTED).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        //then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.allBookingUser(1L, state, from, size));
        verify(bookingRepository, never()).findAllByBookerOrderByStartDesc(Mockito.any(User.class), Mockito.any(Pageable.class));
        assertThat(exception.getMessage(), containsString("Unknown state: " + state));
    }

    @Test
    @DirtiesContext
    void allBookingOwner_shouldNotGetBooking_whenUserNotFound() {
        //given
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        Item item = Item.builder().id(2L).name("item").description("item").available(true).build();
        Integer from = 0;
        Integer size = 2;
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("end").descending());
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.allBookingOwner(1L, "ALL", from, size));
        verify(bookingRepository, never()).findAllByItem_OwnerOrderByStartDesc(user, pageable);
        assertThat(exception.getMessage(), containsString("Пользователь с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void allBookingOwner_shouldGetBooking_whenDataIsCorrectALL() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(user).build();
        Integer from = 1;
        Integer size = 2;
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("end").descending());
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker).status(Status.WAITING).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByItem_OwnerOrderByStartDesc(Mockito.any(User.class), Mockito.any(Pageable.class))).thenReturn(List.of(booking));
        List<BookingResponseDto> bookingResponseDtos = bookingService.allBookingOwner(Mockito.anyLong(), "ALL", from, size);
        //then
        assertFalse(bookingResponseDtos.isEmpty());
        assertThat(bookingResponseDtos.get(0).getItem(), instanceOf(Item.class));
        assertThat(bookingResponseDtos.get(0).getBooker(), instanceOf(User.class));
        assertEquals(bookingResponseDtos.get(0).getStatus(), Status.WAITING);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findAllByItem_OwnerOrderByStartDesc(Mockito.any(User.class), Mockito.any(Pageable.class));
        verifyNoMoreInteractions(itemRepository, userRepository, bookingRepository);
    }

    @Test
    @DirtiesContext
    void allBookingOwner_shouldGetBooking_whenDataIsCorrectCURRENT() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(user).build();
        Integer from = 1;
        Integer size = 2;
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("end").descending());
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker).status(Status.WAITING).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStart(
                Mockito.any(User.class), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponseDto> bookingResponseDtos = bookingService.allBookingOwner(Mockito.anyLong(), "CURRENT", from, size);
        //then
        assertFalse(bookingResponseDtos.isEmpty());
        assertThat(bookingResponseDtos.get(0).getItem(), instanceOf(Item.class));
        assertThat(bookingResponseDtos.get(0).getBooker(), instanceOf(User.class));
        assertEquals(bookingResponseDtos.get(0).getStatus(), Status.WAITING);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStart(
                Mockito.any(User.class), Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class));
        verifyNoMoreInteractions(itemRepository, userRepository, bookingRepository);
    }

    @Test
    @DirtiesContext
    void allBookingOwner_shouldGetBooking_whenDataIsCorrectFUTURE() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(user).build();
        Integer from = 1;
        Integer size = 2;
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("end").descending());
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker).status(Status.WAITING).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(
                Mockito.any(User.class), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponseDto> bookingResponseDtos = bookingService.allBookingOwner(Mockito.anyLong(), "FUTURE", from, size);
        //then
        assertFalse(bookingResponseDtos.isEmpty());
        assertThat(bookingResponseDtos.get(0).getItem(), instanceOf(Item.class));
        assertThat(bookingResponseDtos.get(0).getBooker(), instanceOf(User.class));
        assertEquals(bookingResponseDtos.get(0).getStatus(), Status.WAITING);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(
                Mockito.any(User.class), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class));
        verifyNoMoreInteractions(itemRepository, userRepository, bookingRepository);
    }

    @Test
    @DirtiesContext
    void allBookingOwner_shouldGetBooking_whenDataIsCorrectREJECTED() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(user).build();
        Integer from = 1;
        Integer size = 2;
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("end").descending());
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker).status(Status.REJECTED).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(
                Mockito.any(User.class), Mockito.any(Status.class), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<BookingResponseDto> bookingResponseDtos = bookingService.allBookingOwner(Mockito.anyLong(), "REJECTED", from, size);
        //then
        assertFalse(bookingResponseDtos.isEmpty());
        assertThat(bookingResponseDtos.get(0).getItem(), instanceOf(Item.class));
        assertThat(bookingResponseDtos.get(0).getBooker(), instanceOf(User.class));
        assertEquals(bookingResponseDtos.get(0).getStatus(), Status.REJECTED);
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(bookingRepository, times(1)).findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(
                Mockito.any(User.class), Mockito.any(Status.class), Mockito.any(Pageable.class));
        verifyNoMoreInteractions(itemRepository, userRepository, bookingRepository);
    }

    @Test
    @DirtiesContext
    void allBookingOwner_shouldGetBooking_whenDataIsCorrectUnknown() {
        //given
        LocalDateTime start = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end = start.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        Item item = Item.builder().id(2L).name("item").description("item").available(true).build();
        Integer from = 1;
        Integer size = 2;
        String state = "Qwerty";
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(user).status(Status.REJECTED).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        //then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> bookingService.allBookingOwner(1L, state, from, size));
        verify(bookingRepository, never()).findAllByItem_OwnerOrderByStartDesc(Mockito.any(User.class), Mockito.any(Pageable.class));
        assertThat(exception.getMessage(), containsString("Unknown state: " + state));
    }
}