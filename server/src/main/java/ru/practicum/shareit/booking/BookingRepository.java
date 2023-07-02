package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking> {

    List<Booking> findAllByBookerOrderByStartDesc(User user, Pageable pageable);

    List<Booking> findAllByItem_OwnerOrderByStartDesc(User user, Pageable pageable);

    List<Booking> findAllByBookerAndStatusEqualsOrderByStartDesc(User user, Status status, Pageable pageable);

    List<Booking> findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(User user, Status status, Pageable pageable);

    List<Booking> findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStart(User user, LocalDateTime nowS, LocalDateTime nowE, Pageable pageable);

    List<Booking> findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStart(User user, LocalDateTime nowS, LocalDateTime nowE, Pageable pageable);

    List<Booking> findAllByBookerAndEndIsBeforeOrderByStartDesc(User user, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(User user, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByBookerAndStartIsAfterOrderByStartDesc(User user, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(User user, LocalDateTime now, Pageable pageable);

    Booking findFirstByItemAndStartIsBeforeOrStartEqualsOrderByStartDesc(Item item, LocalDateTime nowS, LocalDateTime nowE);

    Booking findFirstByItemAndStartIsAfterOrderByStart(Item item, LocalDateTime now);

    Booking findFirstByBookerAndItemAndEndIsBeforeOrderByEndDesc(User user, Item item, LocalDateTime now);
}
