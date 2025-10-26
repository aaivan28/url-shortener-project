package com.github.aaivan28.url.shortener.admin.application.service;

import com.github.aaivan28.url.shortener.admin.application.exception.UrlNotFoundException;
import com.github.aaivan28.url.shortener.admin.domain.model.PageModel;
import com.github.aaivan28.url.shortener.admin.domain.model.UrlDocumentModel;
import com.github.aaivan28.url.shortener.admin.domain.port.inbound.ReadUrlUsesCases;
import com.github.aaivan28.url.shortener.admin.domain.port.outbound.UrlRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReadUrlService implements ReadUrlUsesCases {

    private final UrlRepository repository;

    @Override
    public UrlDocumentModel getByCode(final String code) {
        return this.repository.getUrlDocument(code)
                .orElseThrow(UrlNotFoundException::new);
    }

    @Override
    public PageModel<UrlDocumentModel> searchUrl(final String searchText, int page, int size) {
        return null;
    }
}
