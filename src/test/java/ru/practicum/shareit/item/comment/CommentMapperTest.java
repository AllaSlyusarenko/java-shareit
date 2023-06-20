package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

class CommentMapperTest {
    @Test
    @DirtiesContext
    void mapToComment() {
        //given
        LocalDateTime created = LocalDateTime.now();
        User owner = User.builder().id(1L).name("name1").email("name1@ya.ru").build();
        User author = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("Item1").available(true).owner(owner).build();
        CommentRequest commentRequest = CommentRequest.builder().text("comment").build();
        //when
        Comment comment = CommentMapper.mapToComment(commentRequest, author, item, created);
        //then
        Assertions.assertNotNull(comment);
        Assertions.assertEquals(comment.getText(), commentRequest.getText());
        Assertions.assertEquals(comment.getItem(), item);
        Assertions.assertEquals(comment.getAuthor(), author);
        Assertions.assertEquals(comment.getCreated(), created);
    }

    @Test
    @DirtiesContext
    void mapToCommentResponse() {
        //given
        LocalDateTime created = LocalDateTime.now();
        User owner = User.builder().id(1L).name("name1").email("name1@ya.ru").build();
        User author = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("Item1").available(true).owner(owner).build();
        Comment comment = Comment.builder().id(1L).text("comment").item(item).author(author).created(created).build();
        //when
        CommentResponse commentResponse = CommentMapper.mapToCommentResponse(comment);
        //then
        Assertions.assertNotNull(commentResponse);
        Assertions.assertEquals(commentResponse.getId(), comment.getId());
        Assertions.assertEquals(commentResponse.getText(), comment.getText());
        Assertions.assertEquals(commentResponse.getAuthorName(), comment.getAuthor().getName());
        Assertions.assertEquals(commentResponse.getCreated(), comment.getCreated());
    }
}