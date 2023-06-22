package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.QRequest;
import ru.practicum.shareit.request.Request;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    @DirtiesContext
    void testQRequest() {
        User user = User.builder().id(1L).name("name").build();
        userRepository.findAll(QUser.user.name.eq(user.getName()));
    }
}