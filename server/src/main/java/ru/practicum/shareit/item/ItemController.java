package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentRequest;
import ru.practicum.shareit.item.comment.CommentResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShort;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto saveItem(@RequestHeader(USER_ID) Long userId,
                            @RequestBody ItemDto itemDto) {
        log.info("Создание новой вещи пользователем с id {}", userId);
        return itemService.saveItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemShort getItemById(@RequestHeader(USER_ID) Long userId,
                                 @PathVariable(value = "itemId") Long id) {
        log.info("Просмотр вещи по идентификатору с id {}", id);
        return itemService.findItemById(userId, id);
    }

    @GetMapping
    public List<ItemShort> getAllUserItems(@RequestHeader(USER_ID) Long userId,
                                           @RequestParam(value = "from", defaultValue = "0") Integer from,
                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Просмотр владельцем c id {} всех своих вещей", userId);
        return itemService.findAllUserItems(userId, from, size);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(USER_ID) Long userId,
                              @PathVariable(value = "itemId") Long id,
                              @RequestBody ItemDto itemDto) {
        log.info("Обновление вещи c id {}", id);
        return itemService.updateItem(userId, id, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByNameOrDescription(@RequestHeader(USER_ID) Long userId,
                                                    @RequestParam("text") String text,
                                                    @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Поиск вещи по названию или описанию {}", text);
        return itemService.findItemByNameOrDescription(userId, text, from, size);
    }

    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable(value = "id") Long id) {
        log.info("Вещь удалена c id {}", id);
        itemService.deleteItemById(id);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponse saveComment(@RequestHeader(USER_ID) Long userId,
                                       @PathVariable Long itemId,
                                       @RequestBody CommentRequest commentRequest) {
        log.info("Создание нового комментария к вещи c id {}", itemId);
        return itemService.saveComment(userId, itemId, commentRequest);
    }
}