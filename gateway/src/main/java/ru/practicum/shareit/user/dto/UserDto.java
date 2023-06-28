package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Generated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
    @Null
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;
}