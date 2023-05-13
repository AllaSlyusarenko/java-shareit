package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictValidationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {
    HashMap<Long, User> users = new HashMap<>();
    private static Long globalUserId = 1L;

    @Override
    public User saveUser(User userIn) {
        if (isDuplicateEmail(userIn.getEmail())) {
            throw new ConflictValidationException("Данный email уже есть в системе, выберите другой email");
        }
        User userOut = new User(generateUserId(), userIn.getName(), userIn.getEmail());
        users.put(userOut.getId(), userOut);
        return userOut;
    }

    @Override
    public User findUserById(Long id) {
        if (!users.keySet().contains(id)) {
            throw new NotFoundException("Пользователь с данным id не существует");
        }
        return users.get(id);
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(Long id, String name, String email) {
        User userInHistory = findUserById(id);
        if (!userInHistory.getEmail().equals(email) && isDuplicateEmail(email)) {
            throw new ConflictValidationException("Данный email уже есть в системе, выберите другой email" + email);
        }
        if (name == null && email == null) {
            throw new ValidationException("Некорректные данные: пустой name, пустой email, id" + id);
        }
        if (name != null) {
            userInHistory.setName(name);
        }
        if (email != null) {
            userInHistory.setEmail(email);
        }
        users.put(id, userInHistory);
        return userInHistory;
    }

    @Override
    public void deleteUserById(Long id) {
        User user = findUserById(id);
        users.remove(id);
    }

    private Long generateUserId() {
        return globalUserId++;
    }

    private boolean isDuplicateEmail(String email) {
        List<String> userEmails = users.values().stream()
                .map(x -> x.getEmail())
                .collect(Collectors.toList());
        if (userEmails.contains(email)) {
            return true;
        }
        return false; // если false - то всё ок
    }
}
