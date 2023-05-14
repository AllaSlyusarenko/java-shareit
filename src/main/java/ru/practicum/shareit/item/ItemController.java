package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
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
    @ResponseBody
    public ItemDto saveItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                            @Validated(ItemDto.NewItem.class) @RequestBody ItemDto itemDto) {
        log.info("Создание новой вещи");
        Item item = itemMapper.dtoToItem(itemDto);
        item = itemService.saveItem(userId, item);
        return itemMapper.itemToDto(item);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable(value = "id") Long id) {
        log.info("Просмотр вещи по идентификатору");
        Item item = itemService.findItemById(userId, id);
        return ItemMapper.itemToDto(item);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Просмотр владельцем всех вещей");
        List<Item> items = itemService.findAllUserItems(userId);
        return ItemMapper.itemsToDto(items);
    }

}
