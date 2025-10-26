package com.github.aaivan28.url.shortener.admin.infrastructure.adapter.inbound.rest.dto;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Jacksonized
@Builder(toBuilder = true)
public record UrlDocumentDTO(
        String id,
        String urlKey,
        String url,
        boolean enabled,
        String description,
        LocalDate createdAt,
        LocalDate updatedAt) {
}
