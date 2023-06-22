package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.QItem;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class RequestRepositoryTest {
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DirtiesContext
    void testQRequest() {
        Request request = Request.builder().id(1L).description("item").build();
        requestRepository.findAll(QRequest.request.description.eq(request.getDescription()));
    }

    @Test
    @DirtiesContext
    void findAllByRequestorOrderByCreatedDesc() {
        LocalDateTime now1 = LocalDateTime.now();
        LocalDateTime now2 = now1.plusDays(1);
        LocalDateTime now3 = now1.plusDays(2);

        User requestor = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User user = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Request request1 = Request.builder().id(1L).description("description1").requestor(requestor).created(now1).build();
        Request request2 = Request.builder().id(2L).description("description2").requestor(requestor).created(now2).build();
        Request request3 = Request.builder().id(3L).description("description3").requestor(user).created(now3).build();

        userRepository.save(requestor);
        userRepository.save(user);
        requestRepository.save(request1);
        requestRepository.save(request2);
        requestRepository.save(request3);

        List<Request> requests = requestRepository.findAllByRequestorOrderByCreatedDesc(requestor);
        assertEquals(2, requests.size());
        assertEquals(requests.get(0).getId(), 2L);
        assertEquals(requests.get(0).getDescription(), request2.getDescription());
        assertEquals(requests.get(1).getId(), 1L);
        assertEquals(requests.get(1).getDescription(), request1.getDescription());
    }

    @Test
    @DirtiesContext
    void testFindAllByRequestorNot() {
        LocalDateTime now1 = LocalDateTime.now();
        LocalDateTime now2 = now1.plusDays(1);
        LocalDateTime now3 = now1.plusDays(2);

        User requestor = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User user = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Request request1 = Request.builder().id(1L).description("description1").requestor(requestor).created(now1).build();
        Request request2 = Request.builder().id(2L).description("description2").requestor(requestor).created(now2).build();
        Request request3 = Request.builder().id(3L).description("description3").requestor(user).created(now3).build();

        userRepository.save(requestor);
        userRepository.save(user);
        requestRepository.save(request1);
        requestRepository.save(request2);
        requestRepository.save(request3);
        List<Request> requests = requestRepository.findAllByRequestorNot(requestor, Pageable.unpaged());
        assertEquals(1, requests.size());
        assertEquals(requests.get(0).getId(), 3L);
        assertEquals(requests.get(0).getDescription(), request3.getDescription());
    }
}