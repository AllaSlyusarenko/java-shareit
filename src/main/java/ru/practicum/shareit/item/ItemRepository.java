package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ItemRepository {
    public Item saveItem(Long userId, Item item);

    public Item findItemById(Long id);

    public List<Item> findAllUserItems(Long userId);

    public Item updateItem(Long userId, Long id, Item item);

    public List<Item> findItemByNameOrDescription(String text);

    public void deleteItemById(Long id);
}
