package com.github.aaivan28.url.shortener.admin.application.service;

import com.github.aaivan28.url.shortener.admin.domain.port.inbound.DeleteUrlUsesCases;
import com.github.aaivan28.url.shortener.admin.domain.port.outbound.UrlRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteUrlService implements DeleteUrlUsesCases {

    private final UrlRepository repository;
}
