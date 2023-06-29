package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;
    private static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> saveRequest(@RequestHeader(USER_ID) Long userId,
                                              @Validated(RequestDto.NewRequest.class) @RequestBody RequestDto itemRequestDto) {
        log.info("Создание нового запроса на вещь");
        return requestClient.saveRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestByOwnerRequest(@RequestHeader(USER_ID) Long userId) {
        log.info("Просмотр списка запросов пользователя");
        return requestClient.findRequestByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestFromOtherUsers(@RequestHeader(USER_ID) Long userId,
                                                           @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                           @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Просмотр списка запросов других пользователей");
        return requestClient.findRequestFromOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(USER_ID) Long userId,
                                                 @PathVariable(value = "requestId") Long id) {
        log.info("Просмотр запроса по идентификатору");
        return requestClient.findRequestById(userId, id);
    }
}
