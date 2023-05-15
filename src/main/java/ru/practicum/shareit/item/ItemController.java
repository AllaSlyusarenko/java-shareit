package ru.practicum.shareit.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.NewItem;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final Logger log = LoggerFactory.getLogger(ItemController.class);

    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto saveItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                            @Validated(NewItem.class) @RequestBody ItemDto itemDto) {
        log.info("Создание новой вещи");
        Item item = itemMapper.dtoToItem(itemDto);
        item = itemService.saveItem(userId, item);
        return itemMapper.itemToDto(item);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable(value = "itemId") Long id) {
        log.info("Просмотр вещи по идентификатору");
        Item item = itemService.findItemById(id);
        return ItemMapper.itemToDto(item);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Просмотр владельцем всех своих вещей");
        List<Item> items = itemService.findAllUserItems(userId);
        return ItemMapper.itemsToDto(items);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable(value = "itemId") Long id,
                              @RequestBody ItemDto itemDto) {
        log.info("Обновление вещи");
        Item item = itemService.updateItem(userId, id, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable());
        return ItemMapper.itemToDto(item);

    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getItemByNameOrDescription(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestParam("text") String text) {
        log.info("Поиск вещи по названию или описанию");
        List<Item> items = itemService.findItemByNameOrDescription(text);
        return ItemMapper.itemsToDto(items);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItemById(@PathVariable(value = "id") Long id) {
        log.info("Вещь удалена");
        itemService.deleteItemById(id);
    }
}
