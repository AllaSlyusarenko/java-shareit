package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item saveItem(Long userId, Item item) {
        return itemRepository.saveItem(userId, item);
    }

    @Override
    public Item findItemById(Long id) {
        return itemRepository.findItemById(id);
    }

    @Override
    public List<Item> findAllUserItems(Long userId) {
        return itemRepository.findAllUserItems(userId);
    }

    @Override
    public Item updateItem(Long userId, Long id, String name, String description, Boolean available) {
        return itemRepository.updateItem(userId, id, name, description, available);
    }

    @Override
    public List<Item> findItemByNameOrDescription(String text) {
        return itemRepository.findItemByNameOrDescription(text);
    }

    @Override
    public void deleteItemById(Long id) {
        itemRepository.deleteItemById(id);
    }
}
