package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    public User saveUser(User user);

    public User findUserById(Long id);

    public List<User> findAllUsers();

    public User updateUser(Long id, String name, String email);

    public void deleteUserById(Long id);
}