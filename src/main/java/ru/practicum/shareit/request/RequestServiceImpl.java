package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.dto.RequestResponseGetDto;
import ru.practicum.shareit.request.dto.RequestResponsePostDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;

    @Override
    public RequestResponsePostDto saveRequest(Long userId, RequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Request request = RequestMapper.mapToItemRequest(requestDto, user);
        Request requestSave = requestRepository.save(request);
        return RequestMapper.mapToItemResponsePost(requestSave);
    }

    @Override
    public List<RequestResponseGetDto> findRequestByUserId(Long userId) { // свои запросы
        User userRequestor = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        List<Request> requests = requestRepository.findAllByRequestorOrderByCreatedDesc(userRequestor);
        List<RequestResponseGetDto> result = new ArrayList<>();
        for (Request request : requests) {
            List<Item> itemsForEachRequest = itemRepository.findAllByRequest(request);
            List<ItemDto> itemsDtoForEachRequest = ItemMapper.itemsToDto(itemsForEachRequest);
            result.add(RequestMapper.mapToItemResponseGet(request, itemsDtoForEachRequest));
        }
        return result;
    }

    @Override
    public List<RequestResponseGetDto> findRequestFromOtherUsers(Long userId, Integer from, Integer size) { //запросы других пользователей
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        if (from < 0 || size <= 0) {
            throw new ValidationException("from должно быть неотрицательное и size положительное");
        }
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("created").descending());
        List<Request> requests = new ArrayList<>(requestRepository.findAllByRequestorNot(user, pageable));
        List<RequestResponseGetDto> result = new ArrayList<>();
        if (!requests.isEmpty()) {
            for (Request request : requests) {
                List<Item> itemsForEachRequest = itemRepository.findAllByRequest(request);
                List<ItemDto> itemsDtoForEachRequest = ItemMapper.itemsToDto(itemsForEachRequest);
                result.add(RequestMapper.mapToItemResponseGet(request, itemsDtoForEachRequest));
            }
        }
        return result;
    }

    @Override
    public RequestResponseGetDto findRequestById(Long userId, Long id) { // любой пользователь - ItemDto
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Request request = requestRepository.findById(id).orElseThrow(() -> new NotFoundException("Запрос с id " + id + " не найден"));
        List<Item> items = itemRepository.findAllByRequest(request);
        List<ItemDto> itemsDtoForRequest = ItemMapper.itemsToDto(items);
        return RequestMapper.mapToItemResponseGet(request, itemsDtoForRequest);
    }
}