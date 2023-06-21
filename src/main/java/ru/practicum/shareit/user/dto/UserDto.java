package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.*;

@Generated
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
    @Email(groups = {NewUser.class})
    private String email;
}