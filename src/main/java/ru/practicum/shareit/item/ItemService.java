package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    public Item saveItem(Long userId, Item item);

    public Item findItemById(Long id);

    public List<Item> findAllUserItems(Long userId);

    public Item updateItem(Long userId, Long id, String name, String description, Boolean available);

    public List<Item> findItemByNameOrDescription(String text);

    public void deleteItemById(Long id);
}
