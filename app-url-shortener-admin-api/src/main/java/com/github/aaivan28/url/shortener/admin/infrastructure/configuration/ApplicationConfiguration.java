package com.github.aaivan28.url.shortener.admin.infrastructure.configuration;

import com.github.aaivan28.url.shortener.admin.application.UrlService;
import com.github.aaivan28.url.shortener.admin.application.UrlServiceImpl;
import com.github.aaivan28.url.shortener.admin.domain.port.outbound.UrlRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    UrlService urlService(final UrlRepository repository) {
        return new UrlServiceImpl(repository);
    }
}
