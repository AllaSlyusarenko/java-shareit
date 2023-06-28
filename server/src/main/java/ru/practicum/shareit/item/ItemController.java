package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Generated;
import ru.practicum.shareit.item.comment.CommentRequest;
import ru.practicum.shareit.item.comment.CommentResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.item.dto.NewItem;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto saveItem(@RequestHeader(USER_ID) Long userId,
                            @Validated(NewItem.class) @RequestBody ItemDto itemDto) {
        log.info("Создание новой вещи");
        return itemService.saveItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemShort getItemById(@RequestHeader(USER_ID) @Positive Long userId,
                                 @PathVariable(value = "itemId") @Positive Long id) {
        log.info("Просмотр вещи по идентификатору");
        return itemService.findItemById(userId, id);
    }

    @GetMapping
    public List<ItemShort> getAllUserItems(@RequestHeader(USER_ID) @Positive Long userId,
                                           @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Просмотр владельцем всех своих вещей");
        return itemService.findAllUserItems(userId, from, size);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(USER_ID) @Positive Long userId,
                              @PathVariable(value = "itemId") @Positive Long id,
                              @RequestBody ItemDto itemDto) {
        log.info("Обновление вещи");
        return itemService.updateItem(userId, id, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByNameOrDescription(@RequestHeader(USER_ID) @Positive Long userId,
                                                    @RequestParam("text") String text,
                                                    @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                    @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Поиск вещи по названию или описанию");
        return itemService.findItemByNameOrDescription(userId, text, from, size);
    }

    @Generated
    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable(value = "id") @Positive Long id) {
        log.info("Вещь удалена");
        itemService.deleteItemById(id);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponse saveComment(@RequestHeader(USER_ID) @Positive Long userId,
                                       @PathVariable(value = "itemId") @Positive Long id,
                                       @Valid @RequestBody CommentRequest commentRequest) {
        log.info("Создание новой вещи");
        return itemService.saveComment(userId, id, commentRequest);
    }
}