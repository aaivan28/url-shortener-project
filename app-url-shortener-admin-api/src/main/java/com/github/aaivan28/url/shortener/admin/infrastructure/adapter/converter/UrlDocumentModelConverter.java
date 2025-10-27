package com.github.aaivan28.url.shortener.admin.infrastructure.adapter.converter;

import com.github.aaivan28.url.shortener.admin.domain.model.UrlDocumentModel;
import com.github.aaivan28.url.shortener.admin.infrastructure.adapter.outbound.persistence.model.UrlDocument;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class UrlDocumentModelConverter implements Converter<UrlDocument, UrlDocumentModel> {
    @Override
    public UrlDocumentModel convert(final @NonNull UrlDocument urlDocument) {
        final ZoneId zoneId = ZoneId.systemDefault();

        return UrlDocumentModel.builder()
                .code(urlDocument.getKey())
                .url(urlDocument.getUrl())
                .description(urlDocument.getDescription())
                .enabled(urlDocument.isEnabled())
                .createdAt(LocalDateTime.ofInstant(urlDocument.getCreatedAt(), zoneId))
                .updatedAt(LocalDateTime.ofInstant(urlDocument.getUpdatedAt(), zoneId))
                .build();
    }
}
