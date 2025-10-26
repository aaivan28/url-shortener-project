package com.github.aaivan28.url.shortener.admin.domain.port.inbound;

import com.github.aaivan28.url.shortener.admin.domain.model.CreateUrlDocumentModel;
import com.github.aaivan28.url.shortener.admin.domain.model.UrlDocumentModel;

public interface WriteUrlUsesCases {

    UrlDocumentModel createUrlDetail(final CreateUrlDocumentModel createUrlDocumentModel);
}
