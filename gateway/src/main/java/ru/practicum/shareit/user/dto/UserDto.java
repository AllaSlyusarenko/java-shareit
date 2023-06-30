package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
    @Null(groups = {NewUser.class})
    private Long id;

    @NotBlank(groups = {NewUser.class})
    private String name;

    @NotBlank(groups = {NewUser.class})
    @Email(groups = {NewUser.class, UpdateUser.class})
    private String email;
}