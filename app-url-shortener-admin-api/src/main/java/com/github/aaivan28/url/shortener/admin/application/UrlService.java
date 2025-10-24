package com.github.aaivan28.url.shortener.admin.application;

import com.github.aaivan28.url.shortener.admin.domain.port.inbound.DeleteUrlUseCase;
import com.github.aaivan28.url.shortener.admin.domain.port.inbound.GetUrlDetailUseCase;
import com.github.aaivan28.url.shortener.admin.domain.port.inbound.SearchUrlUseCase;

public interface UrlService extends SearchUrlUseCase, GetUrlDetailUseCase, DeleteUrlUseCase {
}
