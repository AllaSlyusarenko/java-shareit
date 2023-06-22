package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DirtiesContext
    void findAllByItem() {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(user).build();
        Comment comment = Comment.builder().id(1L).text("good").item(item).author(booker).created(now).build();
        userRepository.save(user);
        userRepository.save(booker);
        itemRepository.save(item);
        commentRepository.save(comment);
        List<Comment> comments = commentRepository.findAllByItem(item);
        assertEquals(1, comments.size());
        assertThat(comments.get(0).getItem(), instanceOf(Item.class));
        assertEquals(comments.get(0).getText(), comment.getText());
        assertEquals(comments.get(0).getAuthor().getName(), booker.getName());
    }

    @Test
    @DirtiesContext
    void testQComment() {
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(user).build();
        Comment comment = Comment.builder().text("good").item(item).author(user).created(LocalDateTime.now()).build();
        commentRepository.findAll(QComment.comment.text.eq(comment.getText()));
    }
}