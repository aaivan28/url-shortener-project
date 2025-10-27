package com.github.aaivan28.url.shortener.admin.infrastructure.adapter.converter;

import com.github.aaivan28.url.shortener.admin.domain.model.UrlDocumentModel;
import com.github.aaivan28.url.shortener.admin.infrastructure.adapter.inbound.rest.dto.UrlDocumentDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UrlDocumentDTOConverter implements Converter<UrlDocumentModel, UrlDocumentDTO> {

    @Override
    public UrlDocumentDTO convert(final @NonNull UrlDocumentModel urlDocumentModel) {
        return UrlDocumentDTO.builder()
                .code(urlDocumentModel.code())
                .url(urlDocumentModel.url())
                .description(urlDocumentModel.description())
                .enabled(urlDocumentModel.enabled())
                .createdAt(urlDocumentModel.createdAt())
                .updatedAt(urlDocumentModel.updatedAt())
                .build();
    }
}
