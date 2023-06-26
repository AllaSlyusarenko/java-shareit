package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class RequestMapperTest {

    @Test
    @DirtiesContext
    void mapToItemRequest() {
        //given
        User user = User.builder().id(1L).name("name1").email("name1@ya.ru").build();
        RequestDto requestDto = RequestDto.builder().id(1L).description("good").build();
        //when
        Request request = RequestMapper.mapToItemRequest(requestDto, user);
        //then
        Assertions.assertNotNull(request);
        Assertions.assertEquals(request.getDescription(), requestDto.getDescription());
        Assertions.assertEquals(request.getRequestor(), user);
    }

    @Test
    @DirtiesContext
    void mapToItemResponsePost() {
        //given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder().id(1L).name("name1").email("name1@ya.ru").build();
        Request request = Request.builder().id(1L).description("good").requestor(user).build();
        //when
        RequestResponsePostDto requestResponsePostDto = RequestMapper.mapToItemResponsePost(request);
        //then
        Assertions.assertNotNull(requestResponsePostDto);
        Assertions.assertEquals(requestResponsePostDto.getId(), request.getId());
        Assertions.assertEquals(requestResponsePostDto.getDescription(), request.getDescription());
        Assertions.assertEquals(requestResponsePostDto.getRequestor(), request.getRequestor());
        Assertions.assertEquals(requestResponsePostDto.getCreated(), request.getCreated());
    }

    @Test
    @DirtiesContext
    void mapToItemResponseGet() {
        //given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder().id(1L).name("name1").email("name1@ya.ru").build();
        Request request = Request.builder().id(1L).description("good").requestor(user).build();
        List<ItemDto> items = new ArrayList<>();
        //when
        RequestResponseGetDto requestResponseGetDto = RequestMapper.mapToItemResponseGet(request, items);
        //then
        Assertions.assertNotNull(requestResponseGetDto);
        Assertions.assertEquals(requestResponseGetDto.getId(), request.getId());
        Assertions.assertEquals(requestResponseGetDto.getDescription(), request.getDescription());
        Assertions.assertEquals(requestResponseGetDto.getCreated(), request.getCreated());
        Assertions.assertEquals(requestResponseGetDto.getItems(), items);
    }
}