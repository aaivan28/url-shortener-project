package com.github.aaivan28.url.shortener.admin.domain.model;

import lombok.Builder;

import java.util.Collection;

@Builder(toBuilder = true)
public record PageModel<T>(
        Collection<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last){
}
