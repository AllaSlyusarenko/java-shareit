package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.Generated;


@Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDto {

    private Long id;


    private String name;

    private String email;
}