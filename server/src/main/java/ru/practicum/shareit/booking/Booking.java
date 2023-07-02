package ru.practicum.shareit.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.utility.Constants;

import javax.persistence.*;
import java.time.LocalDateTime;

@Generated
@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDateTime start;

    @Column(name = "end_date")
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDateTime end;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "booker_id")
    private User booker;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
