package com.github.aaivan28.url.shortener.admin.infrastructure.adapter.inbound.rest.dto;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Jacksonized
@Builder(toBuilder = true)
public record UrlDocumentDTO(
        String code,
        String url,
        boolean enabled,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
