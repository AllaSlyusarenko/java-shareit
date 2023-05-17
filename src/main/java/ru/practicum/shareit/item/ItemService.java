package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    public Item saveItem(Long userId, Item item);

    public Item findItemById(Long id);

    public List<Item> findAllUserItems(Long userId);

    public Item updateItem(Long userId, Long id, Item item);

    public List<Item> findItemByNameOrDescription(Long userId, String text);

    public void deleteItemById(Long id);
}
