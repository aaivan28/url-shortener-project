package com.github.aaivan28.url.shortener.admin.infrastructure.adapter.inbound.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder(toBuilder = true)
public record CreateUrlDocumentRequestDTO(
        @NotBlank
        String url,
        boolean enabled,
        @NotBlank
        String description) {
}
