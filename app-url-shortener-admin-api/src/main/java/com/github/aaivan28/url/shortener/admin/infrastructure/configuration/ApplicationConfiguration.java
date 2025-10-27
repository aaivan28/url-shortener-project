package com.github.aaivan28.url.shortener.admin.infrastructure.configuration;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.aaivan28.url.shortener.admin.application.service.DeleteUrlService;
import com.github.aaivan28.url.shortener.admin.application.service.ReadUrlService;
import com.github.aaivan28.url.shortener.admin.application.service.UrlDocumentBuilderService;
import com.github.aaivan28.url.shortener.admin.application.service.WriteUrlService;
import com.github.aaivan28.url.shortener.admin.domain.port.inbound.DeleteUrlUsesCases;
import com.github.aaivan28.url.shortener.admin.domain.port.inbound.ReadUrlUsesCases;
import com.github.aaivan28.url.shortener.admin.domain.port.inbound.WriteUrlUsesCases;
import com.github.aaivan28.url.shortener.admin.domain.port.outbound.UrlRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ApplicationConfiguration {

    @Bean
    Clock clockConfiguration() {
        return Clock.systemUTC();
    }

    @Bean
    UrlDocumentBuilderService urlDocumentBuilderService(final Clock clock) {
        return new UrlDocumentBuilderService(clock);
    }

    @Bean
    ReadUrlUsesCases readUrlUsesCases(final UrlRepository repository) {
        return new ReadUrlService(repository);
    }

    @Bean
    WriteUrlUsesCases writeUrlUsesCases(final UrlRepository repository, final UrlDocumentBuilderService urlBuilderService) {
        return new WriteUrlService(repository, urlBuilderService);
    }

    @Bean
    DeleteUrlUsesCases deleteUrlUsesCases(final UrlRepository repository) {
        return new DeleteUrlService(repository);
    }
}
