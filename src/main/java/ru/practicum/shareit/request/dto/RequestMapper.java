package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class RequestMapper {
    public Request mapToItemRequest(RequestDto requestDto, User user) {
        Request request = new Request();
        request.setDescription(requestDto.getDescription());
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());
        return request;
    }

    public RequestResponsePostDto mapToItemResponsePost(Request request) {
        RequestResponsePostDto requestResponsePostDto = new RequestResponsePostDto();
        requestResponsePostDto.setId(request.getId());
        requestResponsePostDto.setDescription(request.getDescription());
        requestResponsePostDto.setRequestor(request.getRequestor());
        requestResponsePostDto.setCreated(request.getCreated());
        return requestResponsePostDto;
    }

    public RequestResponseGetDto mapToItemResponseGet(Request request, List<ItemDto> items) {
        RequestResponseGetDto requestResponseGetDto = new RequestResponseGetDto();
        requestResponseGetDto.setId(request.getId());
        requestResponseGetDto.setDescription(request.getDescription());
        requestResponseGetDto.setCreated(request.getCreated());
        if (!items.isEmpty()) {
            requestResponseGetDto.setItems(items);
        }
        return requestResponseGetDto;
    }
}