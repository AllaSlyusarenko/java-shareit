package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Test
    @DirtiesContext
    void saveItemTest() {
        //given
        UserDto userDtoIn = UserDto.builder().id(1L).name("name").email("name@ya.ru").build();
        //when
        UserDto userDtoOut = userService.saveUser(userDtoIn);
        //then
        assertNotNull(userDtoOut.getId());
        assertEquals(userDtoOut.getName(), userDtoIn.getName());
        assertEquals(userDtoOut.getEmail(), userDtoIn.getEmail());
    }
}
