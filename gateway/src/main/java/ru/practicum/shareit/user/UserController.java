package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUser;
import ru.practicum.shareit.user.dto.UpdateUser;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Positive;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> saveUser(@Validated(NewUser.class) @RequestBody UserDto userDto) {
        log.debug("Создание пользователя: {}", userDto);
        return userClient.saveUser(userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable(value = "id") @Positive Long id) {
        log.info("Просмотр пользователя по идентификатору: {}", id);
        return userClient.findUserById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Просмотр всех пользователей");
        return userClient.findAllUsers();
    }

    @PatchMapping("/{idUser}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "idUser") @Positive Long id,
                                             @Validated(UpdateUser.class) @RequestBody UserDto userDto) {
        log.info("Обновление пользователя");
        return userClient.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable(value = "id") @Positive Long id) {
        log.info("Пользователь удален");
        return userClient.deleteUserById(id);
    }
}
