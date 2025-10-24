package com.github.aaivan28.url.shortener.admin.domain.port.inbound;

import com.github.aaivan28.url.shortener.admin.domain.model.UrlDocument;

public interface GetUrlDetailUseCase {

    UrlDocument getUrlDetail(final String urlKey);
}
