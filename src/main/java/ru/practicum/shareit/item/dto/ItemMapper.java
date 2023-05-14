package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    public static ItemDto itemToDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwner(),
                item.getRequest()
        );
    }

    public static Item dtoToItem(ItemDto itemDto) {
        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.isAvailable(),
                itemDto.getOwner(),
                itemDto.getRequest()
        );
    }

    public static List<ItemDto> itemsToDto(List<Item> items){
        return items.stream().map(x-> itemToDto(x)).collect(Collectors.toList());
    }
    public static List<Item> dtosToItem(List<ItemDto> dtos){
        return dtos.stream().map(x-> dtoToItem(x)).collect(Collectors.toList());
    }

}
