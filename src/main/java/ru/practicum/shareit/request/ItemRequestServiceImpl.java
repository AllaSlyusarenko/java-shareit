package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemResponseGetDto;
import ru.practicum.shareit.request.dto.ItemResponsePostDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemResponsePostDto saveItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id" + userId + " не найден"));
        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(itemRequestDto, user);
        ItemRequest itemRequestSave = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.mapToItemResponsePost(itemRequestSave);
    }

    @Override
    public List<ItemResponseGetDto> findItemRequestByUserId(Long userId) { // свои запросы
        User userRequestor = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id" + userId + " не найден"));
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorOrderByCreatedDesc(userRequestor);
        List<ItemResponseGetDto> result = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            List<Item> itemsForEachRequest = itemRepository.findAllByRequest(itemRequest);
            List<ItemDto> itemsDtoForEachRequest = ItemMapper.itemsToDto(itemsForEachRequest);
            result.add(ItemRequestMapper.mapToItemResponseGet(itemRequest, itemsDtoForEachRequest));
        }
        return result;
    }

    @Override
    public List<ItemResponseGetDto> findItemRequestFromOtherUsers(Long userId, Long from, Long size) { //запросы других пользователей
        return null;
    }

    @Override
    public ItemResponseGetDto findItemRequestById(Long userId, Long id) { // любой пользователь - ItemDto
        return null;
    }
}
