package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepositoryImpl userRepositoryImpl) {
        this.userRepository = userRepositoryImpl;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.saveUser(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    @Override
    public User updateUser(Long id, User user) {
        return userRepository.updateUser(id, user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteUserById(id);
    }
}
