package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = UserMapper.dtoToUser(userDto);
        user = userRepository.save(user);
        return UserMapper.userToDto(user);
    }

    @Override
    public UserDto findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователь не найден");
        }
        return UserMapper.userToDto(user.get());
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> getAll = userRepository.findAll();
        return UserMapper.usersToDto(getAll);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        Optional<User> user = userRepository.findById(id);
        if (userDto.getEmail() != null) {
            user.get().setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            user.get().setName(userDto.getName());
        }

        User userSave = userRepository.save(user.get());
        return UserMapper.userToDto(userSave);
    }

    @Override
    public void deleteUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        userRepository.delete(user.get());
    }
}
