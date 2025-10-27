package com.github.aaivan28.url.shortener.admin.application.service;

import com.github.aaivan28.url.shortener.admin.domain.model.CreateUrlDocumentModel;
import com.github.aaivan28.url.shortener.admin.domain.model.UrlDocumentModel;
import com.github.aaivan28.url.shortener.admin.domain.port.inbound.WriteUrlUsesCases;
import com.github.aaivan28.url.shortener.admin.domain.port.outbound.UrlRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WriteUrlService implements WriteUrlUsesCases {

    private final UrlRepository repository;
    private final UrlDocumentBuilderService urlBuilderService;

    @Override
    public UrlDocumentModel createUrlDetail(final CreateUrlDocumentModel createUrlDocumentModel) {
        final UrlDocumentModel urlDocumentModel = this.urlBuilderService.createFrom(createUrlDocumentModel, this.repository::existsByKey);
        return this.repository.saveUrlDocument(urlDocumentModel);
    }

}
