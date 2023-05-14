package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserRepositoryImpl;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private UserRepositoryImpl userRepository;
    HashMap<Long, Item> items = new HashMap<>();
    private static Long globalItemId = 1L;

    public ItemRepositoryImpl(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Item saveItem(Long userId, Item item) {
        User user = userRepository.findUserById(userId);
        if (!item.isAvailable()) {
            throw new ValidationException("Поле доступен для аренды отрицательно");
        }
        item.setOwner(userId);
        item.setId(generateItemId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item findItemById(Long userId, Long id) {
        if (!items.keySet().contains(id)) {
            throw new NotFoundException("Вещь с данным id не существует");
        }
        return items.get(id);
    }

    @Override
    public List<Item> findAllUserItems(Long userId) {
        User user = userRepository.findUserById(userId);
        List userItems = items.values().stream()
                .filter(x -> x.getOwner() == userId)
                .collect(Collectors.toList());
        return userItems;
    }

    private Long generateItemId() {
        return globalItemId++;
    }
}
