package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
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
    public ItemDto saveItem(Long userId, ItemDto itemDto) {
        User user = userRepository.findUserById(userId);
        Item item = ItemMapper.dtoToItem(user, itemDto);
        item = itemRepository.saveItem(userId, item);
        return ItemMapper.itemToDto(item);
    }

    @Override
    public ItemDto findItemById(Long id) {
        Item item = itemRepository.findItemById(id);
        //Item item = itemRepository.getReferenceById(id);
        return ItemMapper.itemToDto(item);
    }

    @Override
    public List<ItemDto> findAllUserItems(Long userId) {
        User user = userRepository.findUserById(userId);
        List<Item> items = itemRepository.findAllUserItems(userId);
        return ItemMapper.itemsToDto(items);
    }

    @Override
    public ItemDto updateItem(Long userId, Long id, ItemDto itemDto) {
        User user = userRepository.findUserById(userId);
        Item item = ItemMapper.dtoToItem(user, itemDto);
        item = itemRepository.updateItem(userId, id, item);
        return ItemMapper.itemToDto(item);
    }

    @Override
    public List<ItemDto> findItemByNameOrDescription(Long userId, String text) {
        User user = userRepository.findUserById(userId);
        List<Item> items = itemRepository.findItemByNameOrDescription(text);
        return ItemMapper.itemsToDto(items);
    }

    @Override
    public void deleteItemById(Long id) {
        itemRepository.deleteItemById(id);
    }
}
