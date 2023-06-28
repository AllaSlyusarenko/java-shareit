package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @Test
    @DirtiesContext
    void saveUser_shouldSaveUser_whenDataIsCorrect() throws Exception {
        //given
        UserDto userDtoIn = UserDto.builder().name("name").email("name@ya.ru").build();
        UserDto userDtoOut = UserDto.builder().id(1L).name("name").email("name@ya.ru").build();
        //when
        when(userService.saveUser(Mockito.any(UserDto.class)))
                .thenReturn(userDtoOut);
        //then
        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(userDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(userDtoOut.getName()))
                .andExpect(jsonPath("$.email").value(userDtoOut.getEmail()));
        verify(userService, times(1)).saveUser(Mockito.any(UserDto.class));
    }

    @Test
    @DirtiesContext
    void saveUser_shouldNotSaveUser_whenDataIsWrong() throws Exception {
        //given
        UserDto userDtoIn = UserDto.builder().id(1L).name("name").email("name@ya.ru").build();
        UserDto userDtoOut = UserDto.builder().id(1L).name("name").email("name@ya.ru").build();
        //when
        when(userService.saveUser(Mockito.any(UserDto.class)))
                .thenReturn(userDtoOut);
        //then
        mvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(userDtoIn))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
        verify(userService, never()).saveUser(Mockito.any(UserDto.class));
    }

    @Test
    @DirtiesContext
    void getUserById_shouldGetUser_whenDataIsCorrect() throws Exception {
        //given
        UserDto userDtoOut = UserDto.builder().id(1L).name("name").email("name@ya.ru").build();
        //when
        when(userService.findUserById(Mockito.anyLong()))
                .thenReturn(userDtoOut);
        //then
        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(userDtoOut.getName()))
                .andExpect(jsonPath("$.email").value(userDtoOut.getEmail()));
        verify(userService, times(1)).findUserById(Mockito.anyLong());
    }

    @Test
    @DirtiesContext
    void getUserById_shouldNotGetUser_whenDataIsCorrect() throws Exception {
        //given
        UserDto userDtoOut = UserDto.builder().id(1L).name("name").email("name@ya.ru").build();
        //when
        when(userService.findUserById(Mockito.anyLong()))
                .thenReturn(userDtoOut);
        //then
        mvc.perform(get("/users/qwerty")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
        verify(userService, never()).findUserById(Mockito.anyLong());
    }

    @Test
    @DirtiesContext
    void getAllUsers_shouldGetUser_whenDataIsCorrect() throws Exception {
        //given
        UserDto userDto1 = UserDto.builder().id(1L).name("name1").email("name1@ya.ru").build();
        UserDto userDto2 = UserDto.builder().id(2L).name("name2").email("name2@ya.ru").build();
        //when
        when(userService.findAllUsers())
                .thenReturn(List.of(userDto1, userDto2));
        //then
        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name").value(userDto1.getName()))
                .andExpect(jsonPath("$[0].email").value(userDto1.getEmail()))
                .andExpect(jsonPath("$[1].id", is(userDto2.getId()), Long.class))
                .andExpect(jsonPath("$[1].name").value(userDto2.getName()))
                .andExpect(jsonPath("$[1].email").value(userDto2.getEmail()));
        verify(userService, times(1)).findAllUsers();
    }

    @Test
    @DirtiesContext
    void getAllUsers_shouldNotGetUser_whenDataIsWrong() throws Exception {
        //given
        UserDto userDtoOut = UserDto.builder().id(1L).name("name").email("name@ya.ru").build();
        //when
        when(userService.findUserById(Mockito.anyLong()))
                .thenReturn(userDtoOut);
        //then
        mvc.perform(get("/users/qwerty")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
        verify(userService, never()).findUserById(Mockito.anyLong());
    }

    @Test
    @DirtiesContext
    void updateUser_shouldUpdateUser_whenDataIsCorrect() throws Exception {
        //given
        UserDto userDtoIn = UserDto.builder().id(1L).name("name1").email("name1@ya.ru").build();
        UserDto userDtoOut = UserDto.builder().id(1L).name("name1").email("name1@ya.ru").build();
        //when
        when(userService.updateUser(Mockito.anyLong(), Mockito.any(UserDto.class)))
                .thenReturn(userDtoOut);
        //then
        mvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(userDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id", is(userDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(userDtoOut.getName()))
                .andExpect(jsonPath("$.email").value(userDtoOut.getEmail()));
        verify(userService, times(1)).updateUser(Mockito.anyLong(), Mockito.any(UserDto.class));
    }

    @Test
    @DirtiesContext
    void updateUser_shouldNotUpdateUser_whenDataIsWrong() throws Exception {
        //given
        UserDto userDtoIn = UserDto.builder().id(1L).name("name1").email("name1@ya.ru").build();
        UserDto userDtoOut = UserDto.builder().id(1L).name("name1").email("name1@ya.ru").build();
        //when
        when(userService.updateUser(Mockito.anyLong(), Mockito.any(UserDto.class)))
                .thenReturn(userDtoOut);
        //then
        mvc.perform(patch("/users/qwerty")
                .content(objectMapper.writeValueAsString(userDtoIn))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
        verify(userService, never()).updateUser(Mockito.anyLong(), Mockito.any(UserDto.class));
    }
}