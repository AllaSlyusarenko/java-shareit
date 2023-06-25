package ru.practicum.shareit.utility;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class Pagination extends PageRequest {
    public Pagination(int from, int size, Sort sort) {
        super(from / size, size, sort);
    }
}