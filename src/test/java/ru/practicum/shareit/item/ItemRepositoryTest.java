package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RequestRepository requestRepository;

    @Test
    @DirtiesContext
    void findAllByOwner() {
        User owner1 = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User owner2 = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner1).build();
        Item item2 = Item.builder().id(2L).name("item2").description("item2").available(true).owner(owner1).build();
        Item item3 = Item.builder().id(3L).name("item3").description("item3").available(true).owner(owner2).build();

        userRepository.save(owner1);
        userRepository.save(owner2);
        itemRepository.save(item);
        itemRepository.save(item2);
        itemRepository.save(item3);

        List<Item> itemsOwner1 = itemRepository.findAllByOwner(owner1, Pageable.unpaged());
        assertEquals(2, itemsOwner1.size());
        assertEquals("item", itemsOwner1.get(0).getName());
        assertEquals("item", itemsOwner1.get(0).getDescription());
        List<Item> itemsOwner2 = itemRepository.findAllByOwner(owner2, Pageable.unpaged());
        assertEquals(1, itemsOwner2.size());
        assertEquals("item3", itemsOwner2.get(0).getName());
        assertEquals("item3", itemsOwner2.get(0).getDescription());
    }

    @Test
    @DirtiesContext
    void search() {
        User owner1 = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User owner2 = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("iTEm").description("item").available(true).owner(owner1).build();
        Item item2 = Item.builder().id(2L).name("item2").description("item2").available(true).owner(owner1).build();
        Item item3 = Item.builder().id(3L).name("iteM3").description("item3").available(true).owner(owner2).build();
        Item item4 = Item.builder().id(4L).name("iteM4").description("item4").available(false).owner(owner2).build();

        userRepository.save(owner1);
        userRepository.save(owner2);
        itemRepository.save(item);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.save(item4);
        List<Item> items = itemRepository.search("ITEM", Pageable.unpaged());
        assertEquals(3, items.size());
        assertEquals("iTEm", items.get(0).getName());
        assertEquals("item", items.get(0).getDescription());
        assertEquals("item2", items.get(1).getName());
        assertEquals("item2", items.get(1).getDescription());
        assertEquals("iteM3", items.get(2).getName());
        assertEquals("item3", items.get(2).getDescription());
    }

    @Test
    @DirtiesContext
    void findAllByRequest() {
        User owner = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User user = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Request request = Request.builder().description("description").requestor(user).created(LocalDateTime.now()).build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).request(request).build();
        Item item2 = Item.builder().id(2L).name("item2").description("item2").available(true).owner(owner).request(request).build();
        Item item3 = Item.builder().id(3L).name("item3").description("item3").available(true).owner(owner).request(request).build();
        userRepository.save(owner);
        userRepository.save(user);
        requestRepository.save(request);
        itemRepository.save(item);
        itemRepository.save(item2);
        itemRepository.save(item3);
        List<Item> items = itemRepository.findAllByRequest(request);
        assertEquals(3, items.size());
        assertEquals("item", items.get(0).getName());
        assertEquals("item", items.get(0).getDescription());
        assertEquals("item2", items.get(1).getName());
        assertEquals("item2", items.get(1).getDescription());
        assertEquals("item3", items.get(2).getName());
        assertEquals("item3", items.get(2).getDescription());
    }
}