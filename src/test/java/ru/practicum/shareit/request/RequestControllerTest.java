package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestResponseGetDto;
import ru.practicum.shareit.request.dto.RequestResponsePostDto;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RequestController.class)
class RequestControllerTest {
    @MockBean
    private RequestService requestService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @Test
    @DirtiesContext
    void saveRequest_shouldSaveRequest_whenDataIsCorrect() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        RequestDto requestDto = RequestDto.builder().description("description").build();
        RequestResponsePostDto requestResponsePostDto = RequestResponsePostDto.builder().id(1L)
                .description("description").requestor(new User()).created(now).build();
        //when
        when(requestService.saveRequest(Mockito.anyLong(), Mockito.any(RequestDto.class)))
                .thenReturn(requestResponsePostDto);
        //then
        mvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(requestResponsePostDto.getId()), Long.class))
                .andExpect(jsonPath("$.description").value(requestResponsePostDto.getDescription()))
                .andExpect(jsonPath("$.requestor").isNotEmpty())
                .andExpect(jsonPath("$.created").isNotEmpty());
        verify(requestService, times(1)).saveRequest(Mockito.anyLong(), Mockito.any(RequestDto.class));
    }

    @Test
    @DirtiesContext
    void saveRequest_shouldNotSaveRequest_whenDataWrong() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        RequestDto requestDto = RequestDto.builder().description("description").build();
        RequestResponsePostDto requestResponsePostDto = RequestResponsePostDto.builder().id(1L)
                .description("description").requestor(new User()).created(now).build();
        //when
        when(requestService.saveRequest(Mockito.anyLong(), Mockito.any(RequestDto.class)))
                .thenReturn(requestResponsePostDto);
        //then
        mvc.perform(post("/requests")
                .content(objectMapper.writeValueAsString(requestDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
        verify(requestService, never()).saveRequest(Mockito.anyLong(), Mockito.any(RequestDto.class));
    }

    @Test
    @DirtiesContext
    void getRequestByOwnerRequest_shouldGetRequest_whenDataIsCorrect() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        RequestResponseGetDto requestResponseGetDto1 = RequestResponseGetDto.builder().id(1L)
                .description("description1").created(now).items(new ArrayList<>()).build();
        RequestResponseGetDto requestResponseGetDto2 = RequestResponseGetDto.builder().id(2L)
                .description("description2").created(now).items(new ArrayList<>()).build();
        //when
        when(requestService.findRequestByUserId(Mockito.anyLong()))
                .thenReturn(List.of(requestResponseGetDto1, requestResponseGetDto2));
        //then
        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id", is(requestResponseGetDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].description").value(requestResponseGetDto1.getDescription()))
                .andExpect(jsonPath("$[0].created").isNotEmpty())
                .andExpect(jsonPath("$[0].items").isEmpty())
                .andExpect(jsonPath("$[1].id", is(requestResponseGetDto2.getId()), Long.class))
                .andExpect(jsonPath("$[1].description").value(requestResponseGetDto2.getDescription()))
                .andExpect(jsonPath("$[1].created").isNotEmpty())
                .andExpect(jsonPath("$[1].items").isEmpty());
        verify(requestService, times(1)).findRequestByUserId(Mockito.anyLong());
    }

    @Test
    @DirtiesContext
    void getRequestByOwnerRequest_shouldGetNotRequest_whenDataWrong() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        RequestResponseGetDto requestResponseGetDto1 = RequestResponseGetDto.builder().id(1L)
                .description("description1").created(now).items(new ArrayList<>()).build();
        RequestResponseGetDto requestResponseGetDto2 = RequestResponseGetDto.builder().id(2L)
                .description("description2").created(now).items(new ArrayList<>()).build();
        //when
        when(requestService.findRequestByUserId(Mockito.anyLong()))
                .thenReturn(List.of(requestResponseGetDto1, requestResponseGetDto2));
        //then
        mvc.perform(get("/requests")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
        verify(requestService, never()).findRequestByUserId(Mockito.anyLong());
    }

    @Test
    @DirtiesContext
    void getRequestFromOtherUsers_shouldGetRequest_whenDataIsCorrect() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        RequestResponseGetDto requestResponseGetDto1 = RequestResponseGetDto.builder().id(1L)
                .description("description1").created(now).items(new ArrayList<>()).build();
        RequestResponseGetDto requestResponseGetDto2 = RequestResponseGetDto.builder().id(2L)
                .description("description2").created(now).items(new ArrayList<>()).build();
        //when
        when(requestService.findRequestFromOtherUsers(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(requestResponseGetDto1, requestResponseGetDto2));
        //then
        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "1")
                        .param("size", "2")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id", is(requestResponseGetDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].description").value(requestResponseGetDto1.getDescription()))
                .andExpect(jsonPath("$[0].created").isNotEmpty())
                .andExpect(jsonPath("$[0].items").isEmpty())
                .andExpect(jsonPath("$[1].id", is(requestResponseGetDto2.getId()), Long.class))
                .andExpect(jsonPath("$[1].description").value(requestResponseGetDto2.getDescription()))
                .andExpect(jsonPath("$[1].created").isNotEmpty())
                .andExpect(jsonPath("$[1].items").isEmpty());
        verify(requestService, times(1)).findRequestFromOtherUsers(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    @DirtiesContext
    void getRequestFromOtherUsers_shouldNotGetRequest_whenDataWrong() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        RequestResponseGetDto requestResponseGetDto1 = RequestResponseGetDto.builder().id(1L)
                .description("description1").created(now).items(new ArrayList<>()).build();
        RequestResponseGetDto requestResponseGetDto2 = RequestResponseGetDto.builder().id(2L)
                .description("description2").created(now).items(new ArrayList<>()).build();
        //when
        when(requestService.findRequestFromOtherUsers(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(requestResponseGetDto1, requestResponseGetDto2));
        //then
        mvc.perform(get("/requests/all")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", "qwerty")
                .param("from", "1")
                .param("size", "qwerty")
        ).andExpect(status().isBadRequest());
        verify(requestService, never()).findRequestFromOtherUsers(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    @DirtiesContext
    void getRequestById_shouldGetRequest_whenDataIsCorrect() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        RequestResponseGetDto requestResponseGetDto = RequestResponseGetDto.builder().id(1L)
                .description("description1").created(now).items(new ArrayList<>()).build();
        //when
        when(requestService.findRequestById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(requestResponseGetDto);
        //then
        mvc.perform(get("/requests/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id", is(requestResponseGetDto.getId()), Long.class))
                .andExpect(jsonPath("$.description").value(requestResponseGetDto.getDescription()))
                .andExpect(jsonPath("$.created").isNotEmpty())
                .andExpect(jsonPath("$.items").isEmpty());
        verify(requestService, times(1)).findRequestById(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    @DirtiesContext
    void getRequestById_shouldNotGetRequest_whenDataWrong() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        RequestResponseGetDto requestResponseGetDto = RequestResponseGetDto.builder().id(1L)
                .description("description1").created(now).items(new ArrayList<>()).build();
        //when
        when(requestService.findRequestById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(requestResponseGetDto);
        //then
        mvc.perform(get("/requests/1")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", "qwerty")
        ).andExpect(status().isBadRequest());
        verify(requestService, never()).findRequestById(Mockito.anyLong(), Mockito.anyLong());
    }
}