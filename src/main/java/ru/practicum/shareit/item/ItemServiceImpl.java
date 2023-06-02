package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден");
        }
        Item item = ItemMapper.dtoToItem(user.get(), itemDto);
        Item itemSave = itemRepository.save(item);
        return ItemMapper.itemToDto(itemSave);
    }

    @Override
    public ItemDto findItemById(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        //Item item = itemRepository.getReferenceById(id);
        if (!item.isPresent()) {
            throw new NotFoundException("Вещь с id" + id + " не найдена");
        }
        return ItemMapper.itemToDto(item.get());
    }

    @Override
    public List<ItemDto> findAllUserItems(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден");
        }
        List<Item> items = itemRepository.findAllByOwner(user.get());
        return ItemMapper.itemsToDto(items);
    }

    @Override
    public ItemDto updateItem(Long userId, Long id, ItemDto itemDto) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден");
        }
        Optional<Item> item = itemRepository.findById(id);
        if (itemDto.getName() != null) {
            item.get().setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.get().setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.get().setAvailable(itemDto.getAvailable());
        }
        item.get().setOwner(user.get());
        Item itemSave = itemRepository.save(item.get());
        return ItemMapper.itemToDto(itemSave);
    }

    @Override
    public List<ItemDto> findItemByNameOrDescription(Long userId, String text) {
        if(text.isBlank()){
            return new ArrayList<>();
        }
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException("Пользователь с id" + userId + " не найден");
        }
        List<Item> items = itemRepository.search(text);
        return ItemMapper.itemsToDto(items);
    }

    @Override
    public void deleteItemById(Long id) {
        itemRepository.deleteById(id);
    }
}
