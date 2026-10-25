package org.example.portfolioshowcase.dto.request;

import org.example.portfolioshowcase.entity.WorkStatus;
import org.example.portfolioshowcase.entity.WorkType;

public record AdminWorkListQuery(
        Integer page,
        Integer size,
        WorkType type,
        String category,
        String tag,
        String q,
        Boolean featured,
        WorkStatus status) {
}
