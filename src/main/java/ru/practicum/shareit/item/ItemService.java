package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.CommentRequest;
import ru.practicum.shareit.item.comment.CommentResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShort;

import java.util.List;

public interface ItemService {
    public ItemDto saveItem(Long userId, ItemDto itemDto);

    public ItemShort findItemById(Long userId, Long id);

    public List<ItemShort> findAllUserItems(Long userId, Integer from, Integer size);

    public ItemDto updateItem(Long userId, Long id, ItemDto itemDto);

    public List<ItemDto> findItemByNameOrDescription(Long userId, String text, Integer from, Integer size);

    public void deleteItemById(Long id);

    public CommentResponse saveComment(Long userId, Long id, CommentRequest commentRequest);
}