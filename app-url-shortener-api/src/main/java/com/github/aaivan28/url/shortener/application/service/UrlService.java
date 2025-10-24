package com.github.aaivan28.url.shortener.application.service;

import com.github.aaivan28.url.shortener.application.exception.UrlNotFoundException;
import com.github.aaivan28.url.shortener.domain.port.inbound.usescases.UrlUsesCases;
import com.github.aaivan28.url.shortener.domain.port.outbound.UrlRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UrlService implements UrlUsesCases {

    private final UrlRepository repository;

    @Override
    public String getUrl(final String urlKey) {
        return this.repository.getUrl(urlKey).orElseThrow(() -> new UrlNotFoundException("Key not found: " + urlKey));
    }
}
