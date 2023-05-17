package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Item saveItem(Long userId, Item item) {
        User user = userRepository.findUserById(userId);
        item.setOwner(user);
        return itemRepository.saveItem(userId, item);
    }

    @Override
    public Item findItemById(Long id) {
        return itemRepository.findItemById(id);
    }

    @Override
    public List<Item> findAllUserItems(Long userId) {
        User user = userRepository.findUserById(userId);
        return itemRepository.findAllUserItems(userId);
    }

    @Override
    public Item updateItem(Long userId, Long id, Item item) {
        User user = userRepository.findUserById(userId);
        return itemRepository.updateItem(userId, id, item);
    }

    @Override
    public List<Item> findItemByNameOrDescription(Long userId, String text) {
        User user = userRepository.findUserById(userId);
        return itemRepository.findItemByNameOrDescription(text);
    }

    @Override
    public void deleteItemById(Long id) {
        itemRepository.deleteItemById(id);
    }
}
