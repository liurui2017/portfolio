package org.example.portfolioshowcase.common;

import java.util.List;

import org.springframework.data.domain.Page;

public record PageResult<T>(List<T> items, int page, int size, long total, int totalPages) {

    public static <T> PageResult<T> of(Page<?> source, List<T> items) {
        return new PageResult<>(
                items,
                source.getNumber() + 1,
                source.getSize(),
                source.getTotalElements(),
                source.getTotalPages());
    }
}
