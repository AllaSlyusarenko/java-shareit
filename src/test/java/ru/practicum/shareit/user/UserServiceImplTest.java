package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    @DirtiesContext
    void saveUser_shouldSaveUser_whenDataIsCorrect() {
        //given
        UserDto userDtoIn = UserDto.builder().name("name").email("name@ya.ru").build();
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        //when
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        UserDto userDtoOut = userService.saveUser(userDtoIn);
        //then
        assertThat(userDtoOut, instanceOf(UserDto.class));
        assertEquals(userDtoOut.getName(), userDtoIn.getName());
        assertEquals(userDtoOut.getEmail(), userDtoIn.getEmail());
        assertThat(userDtoOut.getId(), instanceOf(Long.class));
        verify(userRepository, times(1)).save(Mockito.any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DirtiesContext
    void findUserById_shouldNotGetUser_whenUserNotFound() {
        //given
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.findUserById(1L));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        assertThat(exception.getMessage(), containsString("Пользователь с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void findUserById_shouldGetUser_whenDataIsCorrect() {
        //given
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        UserDto userDto = userService.findUserById(1L);
        //then
        assertThat(userDto, instanceOf(UserDto.class));
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getId(), user.getId());
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DirtiesContext
    void findAllUsers_shouldGetUsers_whenDataIsCorrect() {
        //given
        User user1 = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User user2 = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        //when
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        List<UserDto> userDtos = userService.findAllUsers();
        //then
        assertFalse(userDtos.isEmpty());
        assertThat(userDtos.get(0), instanceOf(UserDto.class));
        assertEquals(userDtos.get(0).getName(), user1.getName());
        assertEquals(userDtos.get(0).getEmail(), user1.getEmail());
        assertEquals(userDtos.get(0).getId(), user1.getId());
        assertThat(userDtos.get(1), instanceOf(UserDto.class));
        assertEquals(userDtos.get(1).getName(), user2.getName());
        assertEquals(userDtos.get(1).getEmail(), user2.getEmail());
        assertEquals(userDtos.get(1).getId(), user2.getId());
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DirtiesContext
    void updateUser_shouldNotUpdateUser_whenUserNotFound() {
        //given
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.updateUser(1L, Mockito.any(UserDto.class)));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        assertThat(exception.getMessage(), containsString("Пользователь с id 1 не найден"));
    }

    @Test
    @DirtiesContext
    void updateUser_shouldUpdateUser_whenDataIsCorrect() {
        //given
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        UserDto userDtoIn = UserDto.builder().id(1L).name("name2").email("name2@ya.ru").build();
        User userSave = User.builder().id(1L).name("name2").email("name2@ya.ru").build();
        //when
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(userSave);
        UserDto userDto = userService.updateUser(1L, userDtoIn);
        //then
        assertThat(userDto, instanceOf(UserDto.class));
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getId(), user.getId());
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        verify(userRepository, times(1)).save(Mockito.any(User.class));
        verifyNoMoreInteractions(userRepository);
    }
}