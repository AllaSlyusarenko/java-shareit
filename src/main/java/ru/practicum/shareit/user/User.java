package ru.practicum.shareit.user;

import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-controllers.
 */
public class User {
    @Positive
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String email; // два пользователя не могут иметь одинаковый адрес электр почты
}
