package com.github.aaivan28.url.shortener.admin.application.service;

import com.github.aaivan28.url.shortener.admin.domain.model.CreateUrlDocumentModel;
import com.github.aaivan28.url.shortener.admin.domain.model.UrlDocumentModel;
import com.github.aaivan28.url.shortener.admin.domain.port.outbound.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.github.aaivan28.url.shortener.admin.application.utils.CodeGeneratorUtils.generateUniqueCode;

@RequiredArgsConstructor
public class UrlBuilderService {

    private final UrlRepository urlRepository;
    private final Clock clock;

    public UrlDocumentModel buildUrlDocumentModel(final CreateUrlDocumentModel createUrlDocumentModel) {
        final LocalDateTime now = LocalDateTime.now(this.clock);
        final String code = generateUniqueCode(this.urlRepository::existsByKey);
        return UrlDocumentModel.builder()
                .code(code)
                .url(createUrlDocumentModel.url())
                .description(createUrlDocumentModel.description())
                .enabled(createUrlDocumentModel.enabled())
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}
