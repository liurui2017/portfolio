package org.example.portfolioshowcase.dto.response;

public record CategoryResponse(Long id, String name, String slug, Integer sortOrder, long workCount) {
}
