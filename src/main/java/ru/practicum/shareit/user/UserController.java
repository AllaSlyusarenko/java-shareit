package ru.practicum.shareit.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUser;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@Validated(NewUser.class) @RequestBody UserDto userDto) {
        log.info("Создание нового пользователя");
        User user = UserMapper.dtoToUser(userDto);
        user = userService.saveUser(user);
        return UserMapper.userToDto(user);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable(value = "id") Long id) {
        log.info("Просмотр пользователя по идентификатору");
        User user = userService.findUserById(id);
        return UserMapper.userToDto(user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers() {
        log.info("Просмотр всех пользователей");
        List<User> getAll = userService.findAllUsers();
        return UserMapper.usersToDto(getAll);
    }

    @PatchMapping("/{idUser}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@PathVariable(value = "idUser") Long id,
                              @RequestBody UserDto userDto) {
        log.info("Обновление пользователя");
        User user = userService.updateUser(id, userDto.getName(), userDto.getEmail());
        return UserMapper.userToDto(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserById(@PathVariable(value = "id") Long id) {
        log.info("Пользователь удален");
        userService.deleteUserById(id);
    }
}
