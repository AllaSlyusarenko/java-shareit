package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestResponsePostDto saveRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @Validated(RequestDto.NewRequest.class) @RequestBody RequestDto itemRequestDto) {
        log.info("Создание нового запроса на вещь");
        return requestService.saveRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<RequestResponseGetDto> getRequestByOwnerRequest(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Просмотр списка запросов пользователя");
        return requestService.findRequestByUserId(userId);
    }

    @GetMapping("/all")
    public List<RequestResponseGetDto> getRequestFromOtherUsers(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                 @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Просмотр списка запросов других пользователей");
        return requestService.findRequestFromOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public RequestResponseGetDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable(value = "requestId") Long id) {
        log.info("Просмотр запроса по идентификатору");
        return requestService.findRequestById(userId, id);
    }


}