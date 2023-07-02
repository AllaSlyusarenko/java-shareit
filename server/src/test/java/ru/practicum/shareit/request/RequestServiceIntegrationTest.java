package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestResponsePostDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RequestServiceIntegrationTest {
    @Autowired
    private RequestService requestService;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DirtiesContext
    void saveRequestTest() {
        //given
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        userRepository.save(user);
        RequestDto requestDto = RequestDto.builder().id(1L).description("description").build();
        //when
        RequestResponsePostDto requestResponsePostDto = requestService.saveRequest(user.getId(), requestDto);
        //then
        assertNotNull(requestResponsePostDto.getId());
        assertEquals(requestResponsePostDto.getDescription(), requestDto.getDescription());
        assertEquals(requestResponsePostDto.getRequestor().getName(), user.getName());
        assertNotNull(requestResponsePostDto.getCreated());
    }
}
