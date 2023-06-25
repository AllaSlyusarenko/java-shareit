package ru.practicum.shareit.user.dto;

import lombok.Generated;
import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {
    public UserDto userToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public User dtoToUser(UserDto userDto) {
        return new User(userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }

    @Generated
    public List<UserDto> usersToDto(List<User> users) {
        return users.stream().map(x -> userToDto(x)).collect(Collectors.toList());
    }

    @Generated
    public List<User> dtosToUser(List<UserDto> dtos) {
        return dtos.stream().map(x -> dtoToUser(x)).collect(Collectors.toList());
    }
}