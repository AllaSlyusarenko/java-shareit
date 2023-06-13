package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseGetDto;
import ru.practicum.shareit.request.dto.ItemResponsePostDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponsePostDto saveItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @Validated(ItemRequestDto.NewItemRequest.class) @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Создание нового запроса на вещь");
        return itemRequestService.saveItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemResponseGetDto> getItemRequestByOwnerRequest(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Просмотр списка запросов пользователя");
        return itemRequestService.findItemRequestByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemResponseGetDto> getItemRequestFromOtherUsers(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                 @RequestParam("from") Long from,
                                                                 @RequestParam("size") Long size) {
        log.info("Просмотр списка запросов других пользователей");
        return itemRequestService.findItemRequestFromOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemResponseGetDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable(value = "requestId") Long id) {
        log.info("Просмотр запроса по идентификатору");
        return itemRequestService.findItemRequestById(userId, id);
    }


}