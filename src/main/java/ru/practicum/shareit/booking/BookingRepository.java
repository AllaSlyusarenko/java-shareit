package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerOrderByStartDesc(User user);

    List<Booking> findAllByItem_OwnerOrderByStartDesc(User user);

    List<Booking> findAllByBookerAndStatusEqualsOrderByStartDesc(User user, Status status);

    List<Booking> findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(User user, Status status);

    List<Booking> findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStart(User user, LocalDateTime nowS, LocalDateTime nowE);

    List<Booking> findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStart(User user, LocalDateTime nowS, LocalDateTime nowE);

    List<Booking> findAllByBookerAndEndIsBeforeOrderByStartDesc(User user, LocalDateTime now);

    List<Booking> findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(User user, LocalDateTime now);

    List<Booking> findAllByBookerAndStartIsAfterOrderByStartDesc(User user, LocalDateTime now);

    List<Booking> findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(User user, LocalDateTime now);

    Booking findFirstByItemAndStartIsBeforeOrStartEqualsOrderByStartDesc(Item item, LocalDateTime nowS, LocalDateTime nowE);

    Booking findFirstByItemAndStartIsAfterOrderByStart(Item item, LocalDateTime now);

    Booking findFirstByBookerAndItemAndEndIsBeforeOrderByEndDesc(User user, Item item, LocalDateTime now);
}
