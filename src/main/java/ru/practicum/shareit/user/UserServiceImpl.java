package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepositoryImpl userRepositoryImpl) {
        this.userRepository = userRepositoryImpl;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = UserMapper.dtoToUser(userDto);
        user = userRepository.saveUser(user);
        return UserMapper.userToDto(user);
    }

    @Override
    public UserDto findUserById(Long id) {
        User user = userRepository.findUserById(id);
        return UserMapper.userToDto(user);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> getAll = userRepository.findAllUsers();
        return UserMapper.usersToDto(getAll);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = UserMapper.dtoToUser(userDto);
        user = userRepository.updateUser(id, user);
        return UserMapper.userToDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteUserById(id);
    }
}
