package com.github.aaivan28.url.shortener.admin.application;

import com.github.aaivan28.url.shortener.admin.domain.model.Page;
import com.github.aaivan28.url.shortener.admin.domain.model.UrlDocument;
import com.github.aaivan28.url.shortener.admin.domain.port.outbound.UrlRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlRepository repository;

    @Override
    public UrlDocument getUrlDetail(final String urlKey) {
        return null;
    }

    @Override
    public Page<UrlDocument> searchUrl(final String searchText, int page, int size) {
        return null;
    }
}
