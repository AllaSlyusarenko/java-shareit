package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    public UserDto saveUser(UserDto userDto);

    public UserDto findUserById(Long id);

    public List<UserDto> findAllUsers();

    public UserDto updateUser(Long id, UserDto userDto);

    public void deleteUserById(Long id);
}