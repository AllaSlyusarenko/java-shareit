package ru.practicum.shareit.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.Generated;
import ru.practicum.shareit.utility.Constants;

import javax.persistence.*;
import java.time.LocalDateTime;

@Generated
@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor;

    @Column(name = "created_date")
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDateTime created;
}