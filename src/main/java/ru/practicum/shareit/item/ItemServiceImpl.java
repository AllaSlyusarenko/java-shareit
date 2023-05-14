package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

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
    public Item findItemById(Long userId, Long id) {
        return itemRepository.findItemById(userId, id);
    }

    @Override
    public List<Item> findAllUserItems(Long userId) {
        return itemRepository.findAllUserItems(userId);
    }
}
