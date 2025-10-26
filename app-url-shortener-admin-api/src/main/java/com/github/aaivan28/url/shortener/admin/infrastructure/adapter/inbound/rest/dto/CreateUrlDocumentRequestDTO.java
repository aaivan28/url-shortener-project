package com.github.aaivan28.url.shortener.admin.infrastructure.adapter.inbound.rest.dto;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder(toBuilder = true)
public record CreateUrlDocumentRequestDTO(
        String url,
        boolean enabled,
        String description) {
}
