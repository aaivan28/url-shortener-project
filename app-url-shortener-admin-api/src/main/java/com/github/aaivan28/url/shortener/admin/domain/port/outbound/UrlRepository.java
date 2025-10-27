package com.github.aaivan28.url.shortener.admin.domain.port.outbound;

import com.github.aaivan28.url.shortener.admin.domain.model.PageModel;
import com.github.aaivan28.url.shortener.admin.domain.model.UrlDocumentModel;

import java.util.Optional;

public interface UrlRepository {

    PageModel<UrlDocumentModel> searchUrlDocument(final int page, final int size);
    Optional<UrlDocumentModel> getUrlDocument(final String code);
    UrlDocumentModel saveUrlDocument(final UrlDocumentModel urlDocumentModel);
    void deleteUrlDocument(final String code);
    boolean existsByKey(final String key);
}
