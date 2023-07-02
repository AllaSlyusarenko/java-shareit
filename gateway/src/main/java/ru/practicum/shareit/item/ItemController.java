package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.comment.CommentRequest;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.marker.Create;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;
    private static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> saveItem(@RequestHeader(USER_ID) @Positive Long userId,
                                           @Validated(Create.class) @RequestBody ItemDto itemDto) {
        log.info("Создание новой вещи пользователем с id {}", userId);
        return itemClient.saveItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(USER_ID) @Positive Long userId,
                                              @PathVariable(value = "itemId") @Positive Long id) {
        log.info("Просмотр вещи по идентификатору с id {}", id);
        return itemClient.findItemById(userId, id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserItems(@RequestHeader(USER_ID) @Positive Long userId,
                                                  @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                  @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Просмотр владельцем c id {} всех своих вещей", userId);
        return itemClient.findAllUserItems(userId, from, size);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_ID) @Positive Long userId,
                                             @PathVariable(value = "itemId") @Positive Long id,
                                             @RequestBody ItemDto itemDto) {
        log.info("Обновление вещи c id {}", id);
        return itemClient.updateItem(userId, id, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemByNameOrDescription(@RequestHeader(USER_ID) @Positive Long userId,
                                                             @RequestParam("text") String text,
                                                             @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                             @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Поиск вещи по названию или описанию {}", text);
        return itemClient.findItemByNameOrDescription(userId, text, from, size);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItemById(@PathVariable(value = "id") @Positive Long id) {
        log.info("Вещь удалена c id {}", id);
        return itemClient.deleteItemById(id);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> saveComment(@RequestHeader(USER_ID) @Positive Long userId,
                                              @PathVariable @Positive Long itemId,
                                              @Valid @RequestBody CommentRequest commentRequest) {
        log.info("Создание нового комментария к вещи c id {}", itemId);
        return itemClient.saveComment(userId, itemId, commentRequest);
    }
}
