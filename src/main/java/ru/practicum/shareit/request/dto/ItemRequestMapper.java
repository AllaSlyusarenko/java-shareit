package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {
    public static ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto, User user) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }

    public static ItemResponsePostDto mapToItemResponsePost(ItemRequest itemRequest) {
        ItemResponsePostDto itemResponseDto = new ItemResponsePostDto();
        itemResponseDto.setId(itemRequest.getId());
        itemResponseDto.setDescription(itemRequest.getDescription());
        itemResponseDto.setRequestor(itemRequest.getRequestor());
        itemResponseDto.setCreated(itemRequest.getCreated());
        return itemResponseDto;
    }

    public static ItemResponseGetDto mapToItemResponseGet(ItemRequest itemRequest, List<ItemDto> items) {
        ItemResponseGetDto itemResponseGetDto = new ItemResponseGetDto();
        itemResponseGetDto.setId(itemRequest.getId());
        itemResponseGetDto.setDescription(itemRequest.getDescription());
        itemResponseGetDto.setCreated(itemRequest.getCreated());
        if (!items.isEmpty()) {
            itemResponseGetDto.setItems(items);
        }
        return itemResponseGetDto;
    }
}