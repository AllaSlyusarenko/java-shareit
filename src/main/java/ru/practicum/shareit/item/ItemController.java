package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.NewItem;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto saveItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                            @Validated(NewItem.class) @RequestBody ItemDto itemDto) {
        log.info("Создание новой вещи");
        return itemService.saveItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable(value = "itemId") Long id) {
        log.info("Просмотр вещи по идентификатору");
        return itemService.findItemById(id);
    }

    @GetMapping
    public List<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Просмотр владельцем всех своих вещей");
        return itemService.findAllUserItems(userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable(value = "itemId") Long id,
                              @RequestBody ItemDto itemDto) {
        log.info("Обновление вещи");
        return itemService.updateItem(userId, id, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByNameOrDescription(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestParam("text") String text) {
        log.info("Поиск вещи по названию или описанию");
        return itemService.findItemByNameOrDescription(userId, text);
    }

    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable(value = "id") Long id) {
        log.info("Вещь удалена");
        itemService.deleteItemById(id);
    }
}
