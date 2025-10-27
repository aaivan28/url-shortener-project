package com.github.aaivan28.url.shortener.admin.domain.port.inbound;

public interface DeleteUrlUsesCases {

    void deleteUrl(final String code);
}
