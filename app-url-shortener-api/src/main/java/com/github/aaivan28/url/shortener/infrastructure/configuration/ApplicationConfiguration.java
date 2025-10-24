package com.github.aaivan28.url.shortener.infrastructure.configuration;

import com.github.aaivan28.url.shortener.application.service.UrlService;
import com.github.aaivan28.url.shortener.domain.port.inbound.usescases.UrlUsesCases;
import com.github.aaivan28.url.shortener.domain.port.outbound.UrlRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    UrlUsesCases urlUsesCases(final UrlRepository repository) {
        return new UrlService(repository);
    }

}
