package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto saveUser(@RequestBody UserDto userDto) {
        log.info("Создание нового пользователя: {}", userDto);
        return userService.saveUser(userDto);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable(value = "id") Long id) {
        log.info("Просмотр пользователя по идентификатору: {}", id);
        return userService.findUserById(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Просмотр всех пользователей");
        return userService.findAllUsers();
    }

    @PatchMapping("/{idUser}")
    public UserDto updateUser(@PathVariable(value = "idUser") Long id,
                              @RequestBody UserDto userDto) {
        log.info("Обновление пользователя c id {}", id);
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable(value = "id") Long id) {
        log.info("Пользователь  c id {} удален", id);
        userService.deleteUserById(id);
    }
}