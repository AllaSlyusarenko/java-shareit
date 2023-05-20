package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    public ItemDto saveItem(Long userId, ItemDto itemDto);

    public ItemDto findItemById(Long id);

    public List<ItemDto> findAllUserItems(Long userId);

    public ItemDto updateItem(Long userId, Long id, ItemDto itemDto);

    public List<ItemDto> findItemByNameOrDescription(Long userId, String text);

    public void deleteItemById(Long id);
}
