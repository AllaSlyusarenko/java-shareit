package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.comment.CommentRequest;
import ru.practicum.shareit.item.comment.CommentResponse;
import ru.practicum.shareit.item.dto.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @Test
    @DirtiesContext
    void saveItem_shouldSaveItem_whenDataIsCorrect() throws Exception {
        //given
        ItemDto itemDtoIn = ItemDto.builder().name("name1").description("Item1").available(true).requestId(1L).build();
        ItemDto itemDtoOut = ItemDto.builder().id(1L).name("name1").description("Item1").available(true).requestId(1L).build();
        //when
        when(itemService.saveItem(Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenReturn(itemDtoOut);
        //then
        mvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(itemDtoOut.getName()))
                .andExpect(jsonPath("$.description").value(itemDtoOut.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDtoOut.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(itemDtoOut.getRequestId()));
        verify(itemService, times(1)).saveItem(Mockito.anyLong(), Mockito.any(ItemDto.class));
    }

    @Test
    @DirtiesContext
    void saveItem_shouldNotSaveItem_whenDataIsWrong() throws Exception {
        //given
        ItemDto itemDtoIn = ItemDto.builder().name("name1").description("Item1").available(true).requestId(1L).build();
        ItemDto itemDtoOut = ItemDto.builder().id(1L).name("name1").description("Item1").available(true).requestId(1L).build();
        //when
        when(itemService.saveItem(Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenReturn(itemDtoOut);
        //then
        mvc.perform(post("/items")
                .content(objectMapper.writeValueAsString(itemDtoIn))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
        verify(itemService, never()).saveItem(Mockito.anyLong(), Mockito.any(ItemDto.class));
    }

    @Test
    @DirtiesContext
    void getItemById_shouldGet_whenDataCorrect() throws Exception {
        //given
        BookingShort lastBooking = new BookingShort(1L, 1L);
        BookingShort nextBooking = new BookingShort(2L, 1L);
        ItemShort itemShort = ItemShort.builder()
                .id(1L)
                .name("name1")
                .description("Item1")
                .available(true)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(new ArrayList<>())
                .build();
        //when
        when(itemService.findItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemShort);
        //then
        mvc.perform(get("/items/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemShort.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(itemShort.getName()))
                .andExpect(jsonPath("$.description").value(itemShort.getDescription()))
                .andExpect(jsonPath("$.available").value(itemShort.getAvailable()))
                .andExpect(jsonPath("$.lastBooking").isNotEmpty())
                .andExpect(jsonPath("$.nextBooking").isNotEmpty())
                .andExpect(jsonPath("$.comments").isEmpty());
        verify(itemService, times(1)).findItemById(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    @DirtiesContext
    void getItemById_shouldNotGet_whenDataIsWrongUser() throws Exception {
        //given
        BookingShort lastBooking = new BookingShort(1L, 1L);
        BookingShort nextBooking = new BookingShort(2L, 1L);
        ItemShort itemShort = ItemShort.builder()
                .id(1L)
                .name("name1")
                .description("Item1")
                .available(true)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(new ArrayList<>())
                .build();
        //when
        when(itemService.findItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemShort);
        //then
        mvc.perform(get("/items/1")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
        verify(itemService, never()).findItemById(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    @DirtiesContext
    void getItemById_shouldNotGet_whenDataIsWrongId() throws Exception {
        //given
        BookingShort lastBooking = new BookingShort(1L, 1L);
        BookingShort nextBooking = new BookingShort(2L, 1L);
        ItemShort itemShort = ItemShort.builder()
                .id(1L)
                .name("name1")
                .description("Item1")
                .available(true)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(new ArrayList<>())
                .build();
        //when
        when(itemService.findItemById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemShort);
        //then
        mvc.perform(get("/items/qwerty")
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", "1")
        ).andExpect(status().isBadRequest());
        verify(itemService, never()).findItemById(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    @DirtiesContext
    void getAllUserItems_shouldGet_whenDataCorrect() throws Exception {
        //given
        BookingShort lastBooking = new BookingShort(1L, 1L);
        BookingShort nextBooking = new BookingShort(2L, 1L);
        ItemShort itemShort = ItemShort.builder()
                .id(1L)
                .name("name1")
                .description("Item1")
                .available(true)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(new ArrayList<>())
                .build();
        //when
        when(itemService.findAllUserItems(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(itemShort));
        //then
        mvc.perform(get("/items")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "1")
                        .param("size", "2")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id", is(itemShort.getId()), Long.class))
                .andExpect(jsonPath("$[0].name").value(itemShort.getName()))
                .andExpect(jsonPath("$[0].description").value(itemShort.getDescription()))
                .andExpect(jsonPath("$[0].available").value(itemShort.getAvailable()))
                .andExpect(jsonPath("$[0].lastBooking").isNotEmpty())
                .andExpect(jsonPath("$[0].nextBooking").isNotEmpty())
                .andExpect(jsonPath("$[0].comments").isEmpty());
        verify(itemService, times(1)).findAllUserItems(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    @DirtiesContext
    void getAllUserItems_shouldNotGet_whenDataIsWrong() throws Exception {
        //given
        BookingShort lastBooking = new BookingShort(1L, 1L);
        BookingShort nextBooking = new BookingShort(2L, 1L);
        ItemShort itemShort = ItemShort.builder()
                .id(1L)
                .name("name1")
                .description("Item1")
                .available(true)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(new ArrayList<>())
                .build();
        //when
        when(itemService.findAllUserItems(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(itemShort));
        //then
        mvc.perform(get("/items")
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", "1")
                .param("from", "q")
                .param("size", "0")
        ).andExpect(status().isBadRequest());
        verify(itemService, never()).findAllUserItems(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    @DirtiesContext
    void updateItem_shouldUpdateItem_whenDataIsCorrect() throws Exception {
        //given
        ItemDto itemDtoIn = ItemDto.builder().name("name1").description("Item1").available(true).requestId(1L).build();
        ItemDto itemDtoOut = ItemDto.builder().id(1L).name("name1").description("Item1").available(true).requestId(1L).build();
        //when
        when(itemService.updateItem(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenReturn(itemDtoOut);
        //then
        mvc.perform(patch("/items/1")
                        .content(objectMapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoOut.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(itemDtoOut.getName()))
                .andExpect(jsonPath("$.description").value(itemDtoOut.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDtoOut.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(itemDtoOut.getRequestId()));
        verify(itemService, times(1)).updateItem(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ItemDto.class));
    }

    @Test
    @DirtiesContext
    void updateItem_shouldNotUpdateItem_whenDataIsWrong() throws Exception {
        //given
        ItemDto itemDtoIn = ItemDto.builder().name("name1").description("Item1").available(true).requestId(1L).build();
        ItemDto itemDtoOut = ItemDto.builder().id(1L).name("name1").description("Item1").available(true).requestId(1L).build();
        //when
        when(itemService.updateItem(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenReturn(itemDtoOut);
        //then
        mvc.perform(patch("/items/1")
                .content(objectMapper.writeValueAsString(itemDtoIn))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
        verify(itemService, never()).updateItem(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ItemDto.class));
    }

    @Test
    @DirtiesContext
    void getItemByNameOrDescription_shouldGet_whenDataCorrect() throws Exception {
        //given
        ItemDto itemDto1 = ItemDto.builder().id(1L).name("item1").description("Item1").available(true).requestId(1L).build();
        ItemDto itemDto2 = ItemDto.builder().id(2L).name("item2").description("Item2").available(true).requestId(2L).build();
        //when
        when(itemService.findItemByNameOrDescription(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(itemDto1, itemDto2));
        //then
        mvc.perform(get("/items/search")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .param("text", "item")
                        .param("from", "1")
                        .param("size", "2")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name").value(itemDto1.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDto1.getDescription()))
                .andExpect(jsonPath("$[0].available").value(itemDto1.getAvailable()))
                .andExpect(jsonPath("$[0].requestId").value(itemDto1.getRequestId()))
                .andExpect(jsonPath("$[1].id", is(itemDto2.getId()), Long.class))
                .andExpect(jsonPath("$[1].name").value(itemDto2.getName()))
                .andExpect(jsonPath("$[1].description").value(itemDto2.getDescription()))
                .andExpect(jsonPath("$[1].available").value(itemDto2.getAvailable()))
                .andExpect(jsonPath("$[1].requestId").value(itemDto2.getRequestId()))
        ;
        verify(itemService, times(1)).findItemByNameOrDescription(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    @DirtiesContext
    void getItemByNameOrDescription_shouldNotGet_whenDataIsWrong() throws Exception {
        //given
        ItemDto itemDto1 = ItemDto.builder().id(1L).name("item1").description("Item1").available(true).requestId(1L).build();
        ItemDto itemDto2 = ItemDto.builder().id(2L).name("item2").description("Item2").available(true).requestId(2L).build();
        //when
        when(itemService.findItemByNameOrDescription(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(itemDto1, itemDto2));
        //then
        mvc.perform(get("/items/search")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .param("text", "")
                        .param("from", "1")
                        .param("size", "qwerty")
                )
                .andExpect(status().isBadRequest());
        verify(itemService, never()).findItemByNameOrDescription(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    @DirtiesContext
    void saveComment_shouldSave_whenDataCorrect() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        CommentRequest commentRequest = CommentRequest.builder().text("good item").build();
        CommentResponse commentResponse = CommentResponse.builder().id(1L).text("good item").authorName("Name").created(now).build();
        //when
        when(itemService.saveComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(CommentRequest.class)))
                .thenReturn(commentResponse);
        //then
        mvc.perform(post("/items/1/comment")
                        .content(objectMapper.writeValueAsString(commentRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id", is(commentResponse.getId()), Long.class))
                .andExpect(jsonPath("$.text").value(commentResponse.getText()))
                .andExpect(jsonPath("$.authorName").value(commentResponse.getAuthorName()))
                .andExpect(jsonPath("$.created").isNotEmpty());
        verify(itemService, times(1)).saveComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(CommentRequest.class));
    }

    @Test
    @DirtiesContext
    void saveComment_shouldNotSave_whenDataIsWrong() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        CommentRequest commentRequest = CommentRequest.builder().text("good item").build();
        CommentResponse commentResponse = CommentResponse.builder().id(1L).text("good item").authorName("Name").created(now).build();
        //when
        when(itemService.saveComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(CommentRequest.class)))
                .thenReturn(commentResponse);
        //then
        mvc.perform(post("/items/qwerty/comment")
                        .content(objectMapper.writeValueAsString(commentRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                ).andExpect(status().isBadRequest());
        verify(itemService, never()).saveComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(CommentRequest.class));
    }
}