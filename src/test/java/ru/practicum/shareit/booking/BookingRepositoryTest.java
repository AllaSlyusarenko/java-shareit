package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DirtiesContext
    void findAllByBookerOrderByStartDesc() {
        LocalDateTime start1 = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end1 = start1.plusDays(1);
        LocalDateTime start2 = LocalDateTime.of(2025, 3, 15, 22, 15, 15);
        LocalDateTime end2 = start2.plusDays(1);
        User user = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(user).build();
        Booking booking = Booking.builder().id(1L).start(start1).end(end1).item(item).booker(booker).status(Status.WAITING).build();
        Booking booking2 = Booking.builder().id(2L).start(start2).end(end2).item(item).booker(booker).status(Status.APPROVED).build();

        userRepository.save(user);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findAllByBookerOrderByStartDesc(booker, Pageable.unpaged());
        assertEquals(2, bookings.size());
        assertEquals(start2, bookings.get(0).getStart());
        assertEquals(end2, bookings.get(0).getEnd());
        assertThat(bookings.get(0).getItem(), instanceOf(Item.class));
        assertEquals("APPROVED", bookings.get(0).getStatus().name());
        assertEquals(start1, bookings.get(1).getStart());
        assertEquals(end1, bookings.get(1).getEnd());
        assertThat(bookings.get(1).getItem(), instanceOf(Item.class));
        assertEquals("WAITING", bookings.get(1).getStatus().name());
    }

    @Test
    @DirtiesContext
    void findAllByItem_OwnerOrderByStartDesc() {
        LocalDateTime start1 = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end1 = start1.plusDays(1);
        LocalDateTime start2 = LocalDateTime.of(2025, 3, 15, 22, 15, 15);
        LocalDateTime end2 = start2.plusDays(1);
        User owner = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).start(start1).end(end1).item(item).booker(booker).status(Status.WAITING).build();
        Booking booking2 = Booking.builder().id(2L).start(start2).end(end2).item(item).booker(booker).status(Status.APPROVED).build();

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findAllByItem_OwnerOrderByStartDesc(owner, Pageable.unpaged());
        assertEquals(2, bookings.size());
        assertEquals(start2, bookings.get(0).getStart());
        assertEquals(end2, bookings.get(0).getEnd());
        assertThat(bookings.get(0).getItem(), instanceOf(Item.class));
        assertEquals("APPROVED", bookings.get(0).getStatus().name());
        assertEquals(start1, bookings.get(1).getStart());
        assertEquals(end1, bookings.get(1).getEnd());
        assertThat(bookings.get(1).getItem(), instanceOf(Item.class));
        assertEquals("WAITING", bookings.get(1).getStatus().name());
    }

    @Test
    @DirtiesContext
    void findAllByBookerAndStatusEqualsOrderByStartDesc() {
        LocalDateTime start1 = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end1 = start1.plusDays(1);
        LocalDateTime start2 = LocalDateTime.of(2025, 3, 15, 22, 15, 15);
        LocalDateTime end2 = start2.plusDays(1);
        User owner = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).start(start1).end(end1).item(item).booker(booker).status(Status.WAITING).build();
        Booking booking2 = Booking.builder().id(2L).start(start2).end(end2).item(item).booker(booker).status(Status.APPROVED).build();

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findAllByBookerAndStatusEqualsOrderByStartDesc(booker, Status.WAITING, Pageable.unpaged());
        assertEquals(1, bookings.size());
        assertEquals(start1, bookings.get(0).getStart());
        assertEquals(end1, bookings.get(0).getEnd());
        assertThat(bookings.get(0).getItem(), instanceOf(Item.class));
        assertEquals("WAITING", bookings.get(0).getStatus().name());
    }

    @Test
    @DirtiesContext
    void findAllByItem_OwnerAndStatusEqualsOrderByStartDesc() {
        LocalDateTime start1 = LocalDateTime.of(2024, 3, 15, 22, 15, 15);
        LocalDateTime end1 = start1.plusDays(1);
        LocalDateTime start2 = LocalDateTime.of(2025, 3, 15, 22, 15, 15);
        LocalDateTime end2 = start2.plusDays(1);
        User owner = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).start(start1).end(end1).item(item).booker(booker).status(Status.WAITING).build();
        Booking booking2 = Booking.builder().id(2L).start(start2).end(end2).item(item).booker(booker).status(Status.APPROVED).build();

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(owner, Status.WAITING, Pageable.unpaged());
        assertEquals(1, bookings.size());
        assertEquals(start1, bookings.get(0).getStart());
        assertEquals(end1, bookings.get(0).getEnd());
        assertThat(bookings.get(0).getItem(), instanceOf(Item.class));
        assertEquals("WAITING", bookings.get(0).getStatus().name());
    }

    @Test
    @DirtiesContext
    void findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStart() {
        LocalDateTime start1 = LocalDateTime.now().plusMinutes(5);
        LocalDateTime end1 = start1.plusDays(1);
        LocalDateTime start2 = LocalDateTime.of(2025, 3, 15, 22, 15, 15);
        LocalDateTime end2 = start2.plusDays(1);
        LocalDateTime timeBetween = LocalDateTime.now().plusMinutes(10);
        User owner = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).start(start1).end(end1).item(item).booker(booker).status(Status.WAITING).build();
        Booking booking2 = Booking.builder().id(2L).start(start2).end(end2).item(item).booker(booker).status(Status.APPROVED).build();

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStart(
                booker, timeBetween, timeBetween, Pageable.unpaged());
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
        assertEquals(start1, bookings.get(0).getStart());
        assertEquals(end1, bookings.get(0).getEnd());
        assertThat(bookings.get(0).getItem(), instanceOf(Item.class));
        assertEquals("WAITING", bookings.get(0).getStatus().name());
    }

    @Test
    @DirtiesContext
    void findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStart() {
        LocalDateTime start1 = LocalDateTime.now().plusMinutes(5);
        LocalDateTime end1 = start1.plusDays(1);
        LocalDateTime start2 = LocalDateTime.of(2025, 3, 15, 22, 15, 15);
        LocalDateTime end2 = start2.plusDays(1);
        LocalDateTime timeBetween = LocalDateTime.now().plusMinutes(10);
        User owner = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).start(start1).end(end1).item(item).booker(booker).status(Status.WAITING).build();
        Booking booking2 = Booking.builder().id(2L).start(start2).end(end2).item(item).booker(booker).status(Status.APPROVED).build();

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStart(
                owner, timeBetween, timeBetween, Pageable.unpaged());
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
        assertEquals(start1, bookings.get(0).getStart());
        assertEquals(end1, bookings.get(0).getEnd());
        assertThat(bookings.get(0).getItem(), instanceOf(Item.class));
        assertEquals("WAITING", bookings.get(0).getStatus().name());
    }

    @Test
    @DirtiesContext
    void findAllByBookerAndEndIsBeforeOrderByStartDesc() {
        LocalDateTime start1 = LocalDateTime.now().plusMinutes(5);
        LocalDateTime end1 = start1.plusDays(1);
        LocalDateTime start2 = LocalDateTime.of(2025, 3, 15, 22, 15, 15);
        LocalDateTime end2 = start2.plusDays(1);
        LocalDateTime timeAfter = LocalDateTime.now().plusDays(10);
        User owner = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).start(start1).end(end1).item(item).booker(booker).status(Status.WAITING).build();
        Booking booking2 = Booking.builder().id(2L).start(start2).end(end2).item(item).booker(booker).status(Status.APPROVED).build();

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findAllByBookerAndEndIsBeforeOrderByStartDesc(
                booker, timeAfter, Pageable.unpaged());
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
        assertEquals(start1, bookings.get(0).getStart());
        assertEquals(end1, bookings.get(0).getEnd());
        assertThat(bookings.get(0).getItem(), instanceOf(Item.class));
        assertEquals("WAITING", bookings.get(0).getStatus().name());
    }

    @Test
    @DirtiesContext
    void findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc() {
        LocalDateTime start1 = LocalDateTime.now().plusMinutes(5);
        LocalDateTime end1 = start1.plusDays(1);
        LocalDateTime start2 = LocalDateTime.of(2025, 3, 15, 22, 15, 15);
        LocalDateTime end2 = start2.plusDays(1);
        LocalDateTime timeAfter = LocalDateTime.now().plusDays(10);
        User owner = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).start(start1).end(end1).item(item).booker(booker).status(Status.WAITING).build();
        Booking booking2 = Booking.builder().id(2L).start(start2).end(end2).item(item).booker(booker).status(Status.APPROVED).build();

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(
                owner, timeAfter, Pageable.unpaged());
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
        assertEquals(start1, bookings.get(0).getStart());
        assertEquals(end1, bookings.get(0).getEnd());
        assertThat(bookings.get(0).getItem(), instanceOf(Item.class));
        assertEquals("WAITING", bookings.get(0).getStatus().name());
    }

    @Test
    @DirtiesContext
    void findAllByBookerAndStartIsAfterOrderByStartDesc() {
        LocalDateTime start1 = LocalDateTime.now().plusMinutes(5);
        LocalDateTime end1 = start1.plusDays(1);
        LocalDateTime start2 = LocalDateTime.of(2025, 3, 15, 22, 15, 15);
        LocalDateTime end2 = start2.plusDays(1);
        LocalDateTime timeBefore = LocalDateTime.now().minusDays(10);
        User owner = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).start(start1).end(end1).item(item).booker(booker).status(Status.WAITING).build();
        Booking booking2 = Booking.builder().id(2L).start(start2).end(end2).item(item).booker(booker).status(Status.APPROVED).build();

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findAllByBookerAndStartIsAfterOrderByStartDesc(
                booker, timeBefore, Pageable.unpaged());
        assertEquals(2, bookings.size());
        assertEquals(2L, bookings.get(0).getId());
        assertEquals(start2, bookings.get(0).getStart());
        assertEquals(end2, bookings.get(0).getEnd());
        assertThat(bookings.get(0).getItem(), instanceOf(Item.class));
        assertEquals("APPROVED", bookings.get(0).getStatus().name());
        assertEquals(1L, bookings.get(1).getId());
        assertEquals(start1, bookings.get(1).getStart());
        assertEquals(end1, bookings.get(1).getEnd());
        assertThat(bookings.get(1).getItem(), instanceOf(Item.class));
        assertEquals("WAITING", bookings.get(1).getStatus().name());
    }

    @Test
    @DirtiesContext
    void findAllByItem_OwnerAndStartIsAfterOrderByStartDesc() {
        LocalDateTime start1 = LocalDateTime.now().plusMinutes(5);
        LocalDateTime end1 = start1.plusDays(1);
        LocalDateTime start2 = LocalDateTime.of(2025, 3, 15, 22, 15, 15);
        LocalDateTime end2 = start2.plusDays(1);
        LocalDateTime timeBefore = LocalDateTime.now().minusDays(10);
        User owner = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).start(start1).end(end1).item(item).booker(booker).status(Status.WAITING).build();
        Booking booking2 = Booking.builder().id(2L).start(start2).end(end2).item(item).booker(booker).status(Status.APPROVED).build();

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);

        List<Booking> bookings = bookingRepository.findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(
                owner, timeBefore, Pageable.unpaged());
        assertEquals(2, bookings.size());
        assertEquals(2L, bookings.get(0).getId());
        assertEquals(start2, bookings.get(0).getStart());
        assertEquals(end2, bookings.get(0).getEnd());
        assertThat(bookings.get(0).getItem(), instanceOf(Item.class));
        assertEquals("APPROVED", bookings.get(0).getStatus().name());
        assertEquals(1L, bookings.get(1).getId());
        assertEquals(start1, bookings.get(1).getStart());
        assertEquals(end1, bookings.get(1).getEnd());
        assertThat(bookings.get(1).getItem(), instanceOf(Item.class));
        assertEquals("WAITING", bookings.get(1).getStatus().name());
    }

    @Test
    @DirtiesContext
    void findFirstByItemAndStartIsBeforeOrStartEqualsOrderByStartDesc() {
        LocalDateTime start1 = LocalDateTime.now().plusMinutes(5);
        LocalDateTime end1 = start1.plusDays(1);
        LocalDateTime start2 = LocalDateTime.now().plusDays(1);
        LocalDateTime end2 = start2.plusDays(1);
        LocalDateTime timeAfter = LocalDateTime.now().plusDays(10);
        User owner = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).start(start1).end(end1).item(item).booker(booker).status(Status.WAITING).build();
        Booking booking2 = Booking.builder().id(2L).start(start2).end(end2).item(item).booker(booker).status(Status.APPROVED).build();

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);

        Booking bookingResult = bookingRepository.findFirstByItemAndStartIsBeforeOrStartEqualsOrderByStartDesc(
                item, timeAfter, timeAfter);
        assertEquals(2L, bookingResult.getId());
        assertEquals(start2, bookingResult.getStart());
        assertEquals(end2, bookingResult.getEnd());
        assertThat(bookingResult.getItem(), instanceOf(Item.class));
        assertEquals("APPROVED", bookingResult.getStatus().name());
    }

    @Test
    @DirtiesContext
    void findFirstByItemAndStartIsAfterOrderByStart() {
        LocalDateTime start1 = LocalDateTime.now().plusMinutes(5);
        LocalDateTime end1 = start1.plusDays(1);
        LocalDateTime start2 = LocalDateTime.now().plusDays(1);
        LocalDateTime end2 = start2.plusDays(1);
        LocalDateTime timeNow = LocalDateTime.now();
        User owner = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).start(start1).end(end1).item(item).booker(booker).status(Status.WAITING).build();
        Booking booking2 = Booking.builder().id(2L).start(start2).end(end2).item(item).booker(booker).status(Status.APPROVED).build();

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);

        Booking bookingResult = bookingRepository.findFirstByItemAndStartIsAfterOrderByStart(
                item, timeNow);
        assertEquals(1L, bookingResult.getId());
        assertEquals(start1, bookingResult.getStart());
        assertEquals(end1, bookingResult.getEnd());
        assertThat(bookingResult.getItem(), instanceOf(Item.class));
        assertEquals("WAITING", bookingResult.getStatus().name());
    }

    @Test
    @DirtiesContext
    void findFirstByBookerAndItemAndEndIsBeforeOrderByEndDesc() {
        LocalDateTime start1 = LocalDateTime.now().plusMinutes(5);
        LocalDateTime end1 = start1.plusDays(1);
        LocalDateTime start2 = LocalDateTime.now().plusDays(1);
        LocalDateTime end2 = start2.plusDays(1);
        LocalDateTime timeAfter = LocalDateTime.now().plusDays(10);
        User owner = User.builder().id(1L).name("name").email("name@ya.ru").build();
        User booker = User.builder().id(2L).name("name2").email("name2@ya.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).start(start1).end(end1).item(item).booker(booker).status(Status.WAITING).build();
        Booking booking2 = Booking.builder().id(2L).start(start2).end(end2).item(item).booker(booker).status(Status.APPROVED).build();

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
        bookingRepository.save(booking2);

        Booking bookingResult = bookingRepository.findFirstByBookerAndItemAndEndIsBeforeOrderByEndDesc(
                booker, item, timeAfter);
        assertEquals(2L, bookingResult.getId());
        assertEquals(start2, bookingResult.getStart());
        assertEquals(end2, bookingResult.getEnd());
        assertThat(bookingResult.getItem(), instanceOf(Item.class));
        assertEquals("APPROVED", bookingResult.getStatus().name());
    }
}