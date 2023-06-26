package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.comment.CommentRequest;
import ru.practicum.shareit.item.comment.CommentResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ItemServiceIntegrationTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    @DirtiesContext
    void saveItemTest() {
        //given
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        userRepository.save(user);
        ItemDto itemDtoIn = ItemDto.builder().name("item").description("item").available(true).build();
        //when
        ItemDto itemDtoOut = itemService.saveItem(user.getId(), itemDtoIn);
        //then
        assertNotNull(itemDtoOut.getId());
        assertEquals(itemDtoOut.getName(), itemDtoIn.getName());
        assertEquals(itemDtoOut.getDescription(), itemDtoIn.getDescription());
        assertEquals(itemDtoOut.getAvailable(), itemDtoIn.getAvailable());
        assertEquals(itemDtoOut.getRequestId(), itemDtoIn.getRequestId());
    }

    @Test
    @DirtiesContext
    void saveCommentTest() {
        //given
        User owner = User.builder().id(1L).name("name").email("name@ya.ru").build();
        userRepository.save(owner);
        User commentator = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        userRepository.save(commentator);
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        itemRepository.save(item);
        CommentRequest commentRequest = CommentRequest.builder().text("comment").build();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now();
        Booking booking = Booking.builder().id(1L).start(start).end(end).item(item).booker(commentator).build();
        bookingRepository.save(booking);
        //when
        CommentResponse commentResponse = itemService.saveComment(commentator.getId(), item.getId(), commentRequest);
        //then
        assertNotNull(commentResponse.getId());
        assertEquals(commentResponse.getText(), commentRequest.getText());
        assertEquals(commentResponse.getAuthorName(), commentator.getName());
        assertNotNull(commentResponse.getCreated());
    }
}