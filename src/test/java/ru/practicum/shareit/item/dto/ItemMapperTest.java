package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.comment.CommentResponse;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

class ItemMapperTest {
    @Test
    @DirtiesContext
    void itemToDto() {
        //given
        User owner = User.builder().id(1L).name("name1").email("name1@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("Item1").available(true).owner(owner).build();
        //when
        ItemDto itemDto = ItemMapper.itemToDto(item);
        //then
        Assertions.assertNotNull(itemDto);
        Assertions.assertEquals(itemDto.getId(), item.getId());
        Assertions.assertEquals(itemDto.getName(), item.getName());
        Assertions.assertEquals(itemDto.getDescription(), item.getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), item.getAvailable());
    }

    @Test
    @DirtiesContext
    void dtoToItem() {
        //given
        User user = User.builder().id(1L).name("name1").email("name1@ya.ru").build();
        ItemDto itemDto = ItemDto.builder().id(1L).name("item").description("Item1").available(true).build();
        Request request = Request.builder().id(1L).description("description").build();
        //when
        Item item = ItemMapper.dtoToItem(user, itemDto, request);
        //then
        Assertions.assertNotNull(item);
        Assertions.assertEquals(item.getId(), itemDto.getId());
        Assertions.assertEquals(item.getName(), itemDto.getName());
        Assertions.assertEquals(item.getDescription(), itemDto.getDescription());
        Assertions.assertEquals(item.getAvailable(), itemDto.getAvailable());
        Assertions.assertEquals(item.getOwner(), user);
        Assertions.assertEquals(item.getRequest(), request);
    }

    @Test
    @DirtiesContext
    void itemShortDto() {
        //given
        Item item = Item.builder().id(1L).name("item").description("Item1").available(true).build();
        BookingShort lastBooking = BookingShort.builder().id(1L).bookerId(2L).build();
        BookingShort nextBooking = BookingShort.builder().id(2L).bookerId(3L).build();
        List<CommentResponse> comments = new ArrayList<>();
        //when
        ItemShort itemShort = ItemMapper.itemShortDto(item, lastBooking, nextBooking, comments);
        //then
        Assertions.assertNotNull(itemShort);
        Assertions.assertEquals(itemShort.getId(), item.getId());
        Assertions.assertEquals(itemShort.getName(), item.getName());
        Assertions.assertEquals(itemShort.getDescription(), item.getDescription());
        Assertions.assertEquals(itemShort.getAvailable(), item.getAvailable());
        Assertions.assertEquals(itemShort.getLastBooking(), lastBooking);
        Assertions.assertEquals(itemShort.getNextBooking(), nextBooking);
        Assertions.assertEquals(itemShort.getComments(), comments);
    }
}