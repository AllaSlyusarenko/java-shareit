package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.User;

class UserMapperTest {
    @Test
    @DirtiesContext
    void userToDto() {
        //given
        User user = User.builder().id(1L).name("name1").email("name1@ya.ru").build();
        //when
        UserDto userDto = UserMapper.userToDto(user);
        //then
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(userDto.getId(), user.getId());
        Assertions.assertEquals(userDto.getName(), user.getName());
        Assertions.assertEquals(userDto.getEmail(), user.getEmail());
    }

    @Test
    @DirtiesContext
    void dtoToUser() {
        //given
        UserDto userDto = UserDto.builder().id(1L).name("name1").email("name1@ya.ru").build();
        //when
        User user = UserMapper.dtoToUser(userDto);
        //then
        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.getId(), userDto.getId());
        Assertions.assertEquals(user.getName(), userDto.getName());
        Assertions.assertEquals(user.getEmail(), userDto.getEmail());
    }
}