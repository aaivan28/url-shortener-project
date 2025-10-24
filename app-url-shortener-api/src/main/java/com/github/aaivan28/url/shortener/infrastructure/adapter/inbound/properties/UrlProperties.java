package com.github.aaivan28.url.shortener.infrastructure.adapter.inbound.properties;

import com.github.aaivan28.url.shortener.domain.port.inbound.properties.RedirectBaseUrlProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "github.aaivan28.url-shortener")
public record UrlProperties(
        String redirectBaseUrl) implements RedirectBaseUrlProperty {
}
