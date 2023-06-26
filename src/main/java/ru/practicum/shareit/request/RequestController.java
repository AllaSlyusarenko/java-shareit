package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestResponseGetDto;
import ru.practicum.shareit.request.dto.RequestResponsePostDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    private static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestResponsePostDto saveRequest(@RequestHeader(USER_ID) @Positive Long userId,
                                              @Validated(RequestDto.NewRequest.class) @RequestBody RequestDto itemRequestDto) {
        log.info("Создание нового запроса на вещь");
        return requestService.saveRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<RequestResponseGetDto> getRequestByOwnerRequest(@RequestHeader(USER_ID) @Positive Long userId) {
        log.info("Просмотр списка запросов пользователя");
        return requestService.findRequestByUserId(userId);
    }

    @GetMapping("/all")
    public List<RequestResponseGetDto> getRequestFromOtherUsers(@RequestHeader(USER_ID) @Positive Long userId,
                                                                @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                                @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Просмотр списка запросов других пользователей");
        return requestService.findRequestFromOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public RequestResponseGetDto getRequestById(@RequestHeader(USER_ID) @Positive Long userId,
                                                @PathVariable(value = "requestId") @Positive Long id) {
        log.info("Просмотр запроса по идентификатору");
        return requestService.findRequestById(userId, id);
    }
}