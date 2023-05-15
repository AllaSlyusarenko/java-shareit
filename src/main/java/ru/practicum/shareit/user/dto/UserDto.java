package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
    @Null(groups = {NewUser.class})
    @JsonProperty("id")
    private Long id;

    @NotBlank(groups = {NewUser.class})
    @JsonProperty("name")
    private String name;

    @NotBlank(groups = {NewUser.class})
    @Email(groups = {NewUser.class})
    @JsonProperty("email")
    private String email;
}
