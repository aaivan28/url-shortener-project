package com.github.aaivan28.url.shortener.admin.infrastructure.adapter.converter;

import com.github.aaivan28.url.shortener.admin.domain.model.UrlDocumentModel;
import com.github.aaivan28.url.shortener.admin.infrastructure.adapter.outbound.persistence.model.UrlDocument;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class UrlDocumentConverter implements Converter<UrlDocumentModel, UrlDocument> {

    @Override
    public UrlDocument convert(final @NonNull UrlDocumentModel urlDocumentModel) {
        final ZoneOffset zoneOffset = ZoneOffset.UTC;

        return UrlDocument.builder()
                .key(urlDocumentModel.code())
                .url(urlDocumentModel.url())
                .description(urlDocumentModel.description())
                .enabled(urlDocumentModel.enabled())
                .createdAt(urlDocumentModel.createdAt().toInstant(zoneOffset))
                .updatedAt(urlDocumentModel.updatedAt().toInstant(zoneOffset))
                .build();
    }
}
