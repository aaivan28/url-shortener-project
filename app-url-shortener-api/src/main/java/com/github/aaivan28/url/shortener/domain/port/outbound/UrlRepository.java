package com.github.aaivan28.url.shortener.domain.port.outbound;

import java.util.Optional;

public interface UrlRepository {
    Optional<String> getUrl(final String urlKey);
}
