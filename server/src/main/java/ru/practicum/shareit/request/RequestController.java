package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestResponseGetDto;
import ru.practicum.shareit.request.dto.RequestResponsePostDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    private static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public RequestResponsePostDto saveRequest(@RequestHeader(USER_ID) Long userId,
                                              @RequestBody RequestDto itemRequestDto) {
        log.info("Создание нового запроса на вещь");
        return requestService.saveRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<RequestResponseGetDto> getRequestByOwnerRequest(@RequestHeader(USER_ID) Long userId) {
        log.info("Просмотр списка запросов пользователя");
        return requestService.findRequestByUserId(userId);
    }

    @GetMapping("/all")
    public List<RequestResponseGetDto> getRequestFromOtherUsers(@RequestHeader(USER_ID) Long userId,
                                                                @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                                @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Просмотр списка запросов других пользователей");
        return requestService.findRequestFromOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public RequestResponseGetDto getRequestById(@RequestHeader(USER_ID) Long userId,
                                                @PathVariable(value = "requestId") Long id) {
        log.info("Просмотр запроса по идентификатору");
        return requestService.findRequestById(userId, id);
    }
}