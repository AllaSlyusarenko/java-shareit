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
    @Null(groups = {New.class})
    @JsonProperty("id")
    private Long id;

    @NotBlank(groups = {New.class})
    @JsonProperty("name")
    private String name;

    @NotBlank(groups = {New.class})
    @Email(groups = {New.class})
    @JsonProperty("email")
    private String email;

    public interface New {
    }

    interface Exist {
    }

    public interface UpdateAll extends Exist {
    }
}
