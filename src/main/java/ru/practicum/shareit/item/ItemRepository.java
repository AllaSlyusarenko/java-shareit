package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    public Item saveItem(Long userId, Item item);

    public Item findItemById(Long userId, Long id);

    public List<Item> findAllUserItems(Long userId);
}
