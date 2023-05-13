package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;

@Repository
public class ItemRepository {
    HashMap<Long, Item> items = new HashMap<>();
}
