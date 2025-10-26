package com.github.aaivan28.url.shortener.admin.infrastructure.adapter.inbound.rest.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.Collection;

@Value
@Jacksonized
@Builder(toBuilder = true)
public class PaginateResponseDTO<T> {
    Collection<T> content;
    int page;
    int size;
    long totalElements;
    int totalPages;
    boolean first;
    boolean last;
}
