package org.example.portfolioshowcase.dto.response;

import java.time.LocalDateTime;

import org.example.portfolioshowcase.entity.WorkStatus;
import org.example.portfolioshowcase.entity.WorkType;

public record AdminWorkSummaryResponse(
        Long id,
        WorkType type,
        String title,
        String slug,
        WorkStatus status,
        Boolean featured,
        Integer sortOrder,
        CategorySummary category,
        LocalDateTime updatedAt) {
}
