package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestResponseGetDto;
import ru.practicum.shareit.request.dto.RequestResponsePostDto;

import java.util.List;

public interface RequestService {
    public RequestResponsePostDto saveRequest(Long userId, RequestDto itemRequestDto);

    public List<RequestResponseGetDto> findRequestByUserId(Long userId);

    public List<RequestResponseGetDto> findRequestFromOtherUsers(Long userId, Integer from, Integer size);

    public RequestResponseGetDto findRequestById(Long userId, Long id);
}
