package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

//@Repository
public class ItemRepositoryInMemoryImpl {
    private HashMap<Long, Item> items = new HashMap<>();
    private HashMap<Long, List<Item>> itemsByUser = new HashMap<>();
    private Long globalItemId = 1L;


    public Item saveItem(Long userId, Item item) {
        item.setId(generateItemId());
        items.put(item.getId(), item);

        if (!itemsByUser.containsKey(userId)) {
            itemsByUser.put(userId, new ArrayList<>());
        }
        List<Item> userItems = itemsByUser.get(userId);
        userItems.add(item);
        return item;
    }


    public Item findItemById(Long id) {
        Item item = items.get(id);
        if (item == null) {
            throw new NotFoundException("Вещь с данным id не существует");
        }
        return item;
    }


    public List<Item> findAllUserItems(Long userId) {
        return itemsByUser.get(userId);
    }


    public Item updateItem(Long userId, Long id, Item item) {
        Item itemInHistory = findItemById(id);
        if (!userId.equals(itemInHistory.getOwner().getId())) {
            throw new NotFoundException("Редактировать вещь может только её владелец");
        }

        itemsByUser.get(userId).remove(itemInHistory);
        if (item.getName() != null) {
            itemInHistory.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemInHistory.setDescription(item.getDescription());
        }
        if (!isNull(item.getAvailable())) {
            itemInHistory.setAvailable(item.getAvailable());
        }
        items.put(id, itemInHistory);
        itemsByUser.get(userId).add(itemInHistory);

        return itemInHistory;
    }


    public List<Item> findItemByNameOrDescription(String text) {
        List<Item> result = new ArrayList<>();
        if (text.isBlank()) {
            return result;
        }
        String textLowCase = text.toLowerCase();
        List<Item> itemsByName = items.values().stream().filter(x -> x.getName().toLowerCase().contains(textLowCase)).filter(x -> x.getAvailable().equals(true)).collect(Collectors.toList());

        List<Item> itemsByDescription = items.values().stream().filter(x -> x.getDescription().toLowerCase().contains(textLowCase)).filter(x -> x.getAvailable().equals(true)).collect(Collectors.toList());

        result.addAll(itemsByName);
        result.addAll(itemsByDescription);
        result = result.stream().distinct().collect(Collectors.toList());

        return result;
    }


    public void deleteItemById(Long id) {
        Item item = findItemById(id);
        itemsByUser.get(item.getOwner()).remove(item);
        items.remove(id);
    }

    private Long generateItemId() {
        return globalItemId++;
    }
}
