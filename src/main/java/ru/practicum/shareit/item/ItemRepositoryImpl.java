package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

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
        item.setOwner(userId);
        item.setId(generateItemId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item findItemById(Long id) {
        if (!items.keySet().contains(id)) {
            throw new NotFoundException("Вещь с данным id не существует");
        }
        return items.get(id);
    }

    @Override
    public List<Item> findAllUserItems(Long userId) {
        User user = userRepository.findUserById(userId);
        List userItems = items.values().stream()
                .filter(x -> x.getOwner().equals(userId))
                .collect(Collectors.toList());
        return userItems;
    }

    @Override
    public Item updateItem(Long userId, Long id, String name, String description, Boolean available) {
        User user = userRepository.findUserById(userId);
        Item itemInHistory = findItemById(id);
        if (!userId.equals(itemInHistory.getOwner())) {
            throw new NotFoundException("Редактировать вещь может только её владелец");
        }
        if (name == null && description == null && isNull(available)) {
            throw new ValidationException("Некорректные данные для id" + id);
        }
        if (name != null) {
            itemInHistory.setName(name);
        }
        if (description != null) {
            itemInHistory.setDescription(description);
        }
        if (!isNull(available)) {
            itemInHistory.setAvailable(available);
        }
        items.put(id, itemInHistory);
        return itemInHistory;
    }

    @Override
    public List<Item> findItemByNameOrDescription(String text) {
        List<Item> result = new ArrayList<>();
        if (text.isEmpty()) {
            return result;
        }
        String textLowCase = text.toLowerCase();
        List<Item> itemsByName = items.values().stream()
                .filter(x -> x.getName().toLowerCase().contains(textLowCase))
                .filter(x -> x.getAvailable().equals(true))
                .collect(Collectors.toList());

        List<Item> itemsByDescription = items.values().stream()
                .filter(x -> x.getDescription().toLowerCase().contains(textLowCase))
                .filter(x -> x.getAvailable().equals(true))
                .collect(Collectors.toList());

        result.addAll(itemsByName);
        result.addAll(itemsByDescription);
        result = result.stream().distinct().collect(Collectors.toList());

        return result;
    }

    @Override
    public void deleteItemById(Long id) {
        Item item = findItemById(id);
        items.remove(id);
    }

    private Long generateItemId() {
        return globalItemId++;
    }
}
