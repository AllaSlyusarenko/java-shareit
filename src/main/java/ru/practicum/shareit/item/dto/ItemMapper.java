package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemDto itemToDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    public static Item dtoToItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public static List<ItemDto> itemsToDto(List<Item> items) {
        return items.stream().map(x -> itemToDto(x)).collect(Collectors.toList());
    }

    public static List<Item> dtosToItem(List<ItemDto> dtos) {
        return dtos.stream().map(x -> dtoToItem(x)).collect(Collectors.toList());
    }
}
