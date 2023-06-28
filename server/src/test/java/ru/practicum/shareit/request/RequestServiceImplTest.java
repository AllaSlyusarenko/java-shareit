package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestResponseGetDto;
import ru.practicum.shareit.request.dto.RequestResponsePostDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    private RequestService requestService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    public void setUp() {
        requestService = new RequestServiceImpl(userRepository, requestRepository, itemRepository);
    }

    @Test
    @DirtiesContext
    void saveRequest_shouldNotSaveRequest_whenUserNotFound() {
        //given
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> requestService.saveRequest(1L, Mockito.any(RequestDto.class)));
        verify(requestRepository, never()).save(Mockito.any(Request.class));
        assertThat(exception.getMessage(), containsString("Пользователь с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void saveRequest_shouldSaveRequest_whenDataIsCorrect() {
        //given
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        RequestDto requestDto = RequestDto.builder().description("description").build();
        Request request = Request.builder().description("description").requestor(user).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.save(Mockito.any(Request.class))).thenReturn(request);
        RequestResponsePostDto requestResponsePostDto = requestService.saveRequest(1L, requestDto);
        //then
        assertThat(requestResponsePostDto, instanceOf(RequestResponsePostDto.class));
        assertEquals(requestResponsePostDto.getDescription(), requestDto.getDescription());
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(requestRepository, times(1)).save(Mockito.any());
        verifyNoMoreInteractions(userRepository, requestRepository);
    }

    @Test
    @DirtiesContext
    void findRequestByUserId_shouldNotGetRequest_whenUserNotFound() {
        //given
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> requestService.findRequestByUserId(1L));
        verify(requestRepository, never()).findAllByRequestorOrderByCreatedDesc(Mockito.any(User.class));
        assertThat(exception.getMessage(), containsString("Пользователь с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void findRequestByUserId_shouldGetRequest_whenDataIsCorrect() {
        //given
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        Request request = Request.builder().description("description").requestor(user).build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(user).request(request).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequestorOrderByCreatedDesc(Mockito.any(User.class))).thenReturn(List.of(request));
        when(itemRepository.findAllByRequest(Mockito.any(Request.class))).thenReturn(List.of(item));
        List<RequestResponseGetDto> requestResponseGetDtos = requestService.findRequestByUserId(1L);
        //then
        assertFalse(requestResponseGetDtos.isEmpty());
        assertEquals(requestResponseGetDtos.get(0).getId(), request.getId());
        assertEquals(requestResponseGetDtos.get(0).getDescription(), request.getDescription());
        assertThat(requestResponseGetDtos.get(0).getItems().get(0), instanceOf(ItemDto.class));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(requestRepository, times(1)).findAllByRequestorOrderByCreatedDesc(Mockito.any());
        verifyNoMoreInteractions(userRepository, requestRepository);
    }

    @Test
    @DirtiesContext
    void findRequestFromOtherUsers_shouldNotGetRequest_whenUserNotFound() {
        //given
        Integer from = 0;
        Integer size = 2;
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> requestService.findRequestFromOtherUsers(1L, from, size));
        verify(requestRepository, never()).findAllByRequestorNot(Mockito.any(User.class), Mockito.any(Pageable.class));
        assertThat(exception.getMessage(), containsString("Пользователь с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void findRequestFromOtherUsers_shouldGetRequest_whenDataIsCorrect() {
        //given
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User other = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Request request = Request.builder().id(1L).description("description").requestor(other).build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(user).request(request).build();
        Integer from = 0;
        Integer size = 2;
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequestorNot(Mockito.any(User.class), Mockito.any(Pageable.class))).thenReturn(List.of(request));
        when(itemRepository.findAllByRequest(Mockito.any(Request.class))).thenReturn(List.of(item));
        List<RequestResponseGetDto> requestResponseGetDtos = requestService.findRequestFromOtherUsers(1L, from, size);
        //then
        assertFalse(requestResponseGetDtos.isEmpty());
        assertThat(requestResponseGetDtos.get(0), instanceOf(RequestResponseGetDto.class));
        assertEquals(requestResponseGetDtos.get(0).getDescription(), request.getDescription());
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(requestRepository, times(1)).findAllByRequestorNot(Mockito.any(User.class), Mockito.any(Pageable.class));
        verifyNoMoreInteractions(userRepository, requestRepository, itemRepository);
    }

    @Test
    @DirtiesContext
    void findRequestById_shouldNotGetRequest_whenUserNotFound() {
        //given
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> requestService.findRequestById(1L, 1L));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(requestRepository, never()).findById(Mockito.anyLong());
        assertThat(exception.getMessage(), containsString("Пользователь с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void findRequestById_shouldNotGetRequest_whenRequestNotFound() {
        //given
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> requestService.findRequestById(1L, 1L));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(requestRepository, times(1)).findById(Mockito.anyLong());
        assertThat(exception.getMessage(), containsString("Запрос с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void findRequestById_shouldGetRequest_whenDataIsCorrect() {
        //given
        User requestor = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User owner = User.builder().id(1L).name("name").email("name@ya.ru").build();
        Request request = Request.builder().description("description").requestor(requestor).build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).request(request).build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(requestor));
        when(requestRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(request));
        when(itemRepository.findAllByRequest(Mockito.any(Request.class))).thenReturn(List.of(item));
        RequestResponseGetDto requestResponseGetDto = requestService.findRequestById(1L, 1L);
        //then
        assertThat(requestResponseGetDto, instanceOf(RequestResponseGetDto.class));
        assertEquals(requestResponseGetDto.getDescription(), request.getDescription());
        assertThat(requestResponseGetDto.getItems().get(0), instanceOf(ItemDto.class));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(requestRepository, times(1)).findById(Mockito.anyLong());
        verify(itemRepository, times(1)).findAllByRequest(Mockito.any());
        verifyNoMoreInteractions(itemRepository, userRepository, requestRepository);
    }
}