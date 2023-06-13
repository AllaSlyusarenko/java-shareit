package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseGetDto;
import ru.practicum.shareit.request.dto.ItemResponsePostDto;

import java.util.List;

public interface ItemRequestService {
    public ItemResponsePostDto saveItemRequest(Long userId, ItemRequestDto itemRequestDto);

    public List<ItemResponseGetDto> findItemRequestByUserId(Long userId);

    public List<ItemResponseGetDto> findItemRequestFromOtherUsers(Long userId, Long from, Long size);

    public ItemResponseGetDto findItemRequestById(Long userId, Long id);
}
