package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

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

    public static Item dtoToItem(User user, ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(user);
        return item;
    }

    public static List<ItemDto> itemsToDto(List<Item> items) {
        List<ItemDto> dtos = new ArrayList<>();
        for (Item item : items) {
            dtos.add(itemToDto(item));
        }
        return dtos;
    }

    public static ItemShort itemShortDto(Item item, BookingShort lastBooking, BookingShort nextBooking, List<Comment> comments) {
        ItemShort itemShort = new ItemShort();
        itemShort.setId(item.getId());
        itemShort.setName(item.getName());
        itemShort.setDescription(item.getDescription());
        itemShort.setAvailable(item.getAvailable());
        itemShort.setLastBooking(lastBooking);
        itemShort.setNextBooking(nextBooking);
        itemShort.setComments(comments);
        return itemShort;
    }
}
