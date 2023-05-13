package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public static UserDto userToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User dtoToUser(UserDto userDto) {
        return new User(userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static List<UserDto> usersToDto(List<User> users) {
        return users.stream().map(x -> userToDto(x)).collect(Collectors.toList());
    }

    public static List<User> dtosToUser(List<UserDto> dtos) {
        return dtos.stream().map(x -> dtoToUser(x)).collect(Collectors.toList());
    }

}
