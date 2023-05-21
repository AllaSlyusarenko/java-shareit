package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {

    public User saveUser(User user);

    public User findUserById(Long id);

    public List<User> findAllUsers();

    public User updateUser(Long id, User user);

    public void deleteUserById(Long id);
}
