package com.github.aaivan28.url.shortener.admin.domain.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record CreateUrlDocumentModel(
        String url,
        boolean enabled,
        String description) {
}
