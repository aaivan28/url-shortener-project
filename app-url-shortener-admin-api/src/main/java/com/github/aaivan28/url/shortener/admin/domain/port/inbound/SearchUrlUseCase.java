package com.github.aaivan28.url.shortener.admin.domain.port.inbound;

import com.github.aaivan28.url.shortener.admin.domain.model.Page;
import com.github.aaivan28.url.shortener.admin.domain.model.UrlDocument;

public interface SearchUrlUseCase {

    Page<UrlDocument> searchUrl(final String searchText, final int page, final int size);
}
