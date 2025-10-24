package com.github.aaivan28.url.shortener.admin.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UrlDocument(
        String key,
        String url,
        String description,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

}
