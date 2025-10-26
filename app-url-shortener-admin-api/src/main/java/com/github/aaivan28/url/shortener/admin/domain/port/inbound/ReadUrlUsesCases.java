package com.github.aaivan28.url.shortener.admin.domain.port.inbound;

import com.github.aaivan28.url.shortener.admin.domain.model.PageModel;
import com.github.aaivan28.url.shortener.admin.domain.model.UrlDocumentModel;

public interface ReadUrlUsesCases {

    UrlDocumentModel getByCode(final String code);
    PageModel<UrlDocumentModel> searchUrl(final String searchText, final int page, final int size);
}
