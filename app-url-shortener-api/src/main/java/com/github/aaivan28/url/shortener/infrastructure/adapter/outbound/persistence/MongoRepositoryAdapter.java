package com.github.aaivan28.url.shortener.infrastructure.adapter.outbound.persistence;

import com.github.aaivan28.url.shortener.domain.port.outbound.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MongoRepositoryAdapter implements UrlRepository {

    private final ConversionService conversionService;
    private final UrlMongoRepository repository;

    @Override
    public Optional<String> getUrl(final String urlKey) {
        return this.repository.findByKey(urlKey)
                .filter(UrlDocument::isEnabled)
                .map(this::extractUrl);
    }

    private String extractUrl(final UrlDocument urlDocument) {
        return this.conversionService.convert(urlDocument, String.class);
    }
}
