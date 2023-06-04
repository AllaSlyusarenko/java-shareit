package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
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

    public static ItemShort itemShortDto(Item item, BookingShort lastBooking, BookingShort nextBooking) {
        ItemShort itemShort = new ItemShort();
        itemShort.setId(item.getId());
        itemShort.setName(item.getName());
        itemShort.setDescription(item.getDescription());
        itemShort.setAvailable(item.getAvailable());
        itemShort.setLastBooking(lastBooking);
        itemShort.setNextBooking(nextBooking);
        return itemShort;
    }

    public static ItemOwnerDto itemToOwnerDto(Item item, Booking lastBooking, Booking nextBooking) {
        ItemOwnerDto itemOwnerDto = new ItemOwnerDto();
        itemOwnerDto.setId(item.getId());
        itemOwnerDto.setName(item.getName());
        itemOwnerDto.setDescription(item.getDescription());
        itemOwnerDto.setAvailable(item.getAvailable());
        itemOwnerDto.setLastBooking(lastBooking);
        itemOwnerDto.setNextBooking(nextBooking);
        return itemOwnerDto;
    }
}
