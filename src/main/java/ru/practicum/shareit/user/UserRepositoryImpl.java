package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictValidationException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private HashMap<Long, User> users = new HashMap<>();
    private Set<String> emails = new HashSet<>();
    private Long globalUserId = 1L;

    @Override
    public User saveUser(User userIn) {
        if (emails.contains(userIn.getEmail())) {
            throw new ConflictValidationException("Данный email уже есть в системе, выберите другой email");
        }
        User userOut = new User(generateUserId(), userIn.getName(), userIn.getEmail());
        users.put(userOut.getId(), userOut);
        emails.add(userIn.getEmail());
        return userOut;
    }

    @Override
    public User findUserById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с данным id не существует");
        }
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(Long id, User user) {
        User userInHistory = findUserById(id);
        if (!userInHistory.getEmail().equals(user.getEmail()) && emails.contains(user.getEmail())) {
            throw new ConflictValidationException("Данный email уже есть в системе, выберите другой email" + user.getEmail());
        }
        if (user.getName() != null) {
            userInHistory.setName(user.getName());
        }
        if (user.getEmail() != null) {
            emails.remove(userInHistory.getEmail());
            userInHistory.setEmail(user.getEmail());
            emails.add(user.getEmail());
        }
        users.put(id, userInHistory);
        return userInHistory;
    }

    @Override
    public void deleteUserById(Long id) {
        User user = findUserById(id);
        emails.remove(user.getEmail());
        users.remove(id);
    }

    private Long generateUserId() {
        return globalUserId++;
    }
}
