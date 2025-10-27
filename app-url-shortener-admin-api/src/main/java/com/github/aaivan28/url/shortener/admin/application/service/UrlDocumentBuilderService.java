package com.github.aaivan28.url.shortener.admin.application.service;

import com.github.aaivan28.url.shortener.admin.domain.model.CreateUrlDocumentModel;
import com.github.aaivan28.url.shortener.admin.domain.model.UrlDocumentModel;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import static com.github.aaivan28.url.shortener.admin.application.utils.CodeGeneratorUtils.generateUniqueCode;

@RequiredArgsConstructor
public class UrlDocumentBuilderService {

    private final Clock clock;

    public UrlDocumentModel createFrom(final CreateUrlDocumentModel createUrlDocumentModel, final Predicate<String> validateCode) {
        final LocalDateTime now = LocalDateTime.now(this.clock);
        final String code = generateUniqueCode(validateCode);
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
