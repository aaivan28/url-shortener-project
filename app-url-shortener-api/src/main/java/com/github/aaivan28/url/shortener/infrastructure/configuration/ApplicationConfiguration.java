package com.github.aaivan28.url.shortener.infrastructure.configuration;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.aaivan28.url.shortener.application.service.UrlService;
import com.github.aaivan28.url.shortener.domain.port.inbound.usescases.UrlUsesCases;
import com.github.aaivan28.url.shortener.domain.port.outbound.UrlRepository;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    UrlUsesCases urlUsesCases(final UrlRepository repository) {
        return new UrlService(repository);
    }

    @Bean
    ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .findAndAddModules()
                .build();
    }
}
