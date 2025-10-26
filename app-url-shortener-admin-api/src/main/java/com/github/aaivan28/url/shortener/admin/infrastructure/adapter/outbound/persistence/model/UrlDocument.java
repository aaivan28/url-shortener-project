package com.github.aaivan28.url.shortener.admin.infrastructure.adapter.outbound.persistence.model;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
@Document(UrlDocument.COLLECTION_NAME)
public class UrlDocument {
    public static final String COLLECTION_NAME = "shortened_url";

    @Id
    String id;
    @Indexed(unique = true)
    String key;
    String description;
    String url;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    boolean enabled;
}
