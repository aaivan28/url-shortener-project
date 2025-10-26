package com.github.aaivan28.url.shortener.admin.infrastructure.adapter.outbound.persistence.repository;

import com.github.aaivan28.url.shortener.admin.domain.model.PageModel;
import com.github.aaivan28.url.shortener.admin.domain.model.UrlDocumentModel;
import com.github.aaivan28.url.shortener.admin.domain.port.outbound.UrlRepository;
import com.github.aaivan28.url.shortener.admin.infrastructure.adapter.outbound.persistence.model.UrlDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MongoRepositoryAdapter implements UrlRepository {

    private final UrlMongoRepository repository;
    private final ConversionService conversionService;

    @Override
    public PageModel<UrlDocumentModel> searchUrlDocument(final int page, final int size) {
        return null;
    }

    @Override
    public Optional<UrlDocumentModel> getUrlDocument(final String code) {
        return this.repository.findByKey(code)
                .map(this::convert);
    }

    @Override
    public UrlDocumentModel saveUrlDocument(final UrlDocumentModel urlDocumentModel) {
        return this.convert(this.repository.save(this.convert(urlDocumentModel)));
    }

    @Override
    public boolean existsByKey(final String key) {
        return this.repository.existsByKey(key);
    }

    private UrlDocumentModel convert(final UrlDocument urlDocument) {
        return this.conversionService.convert(urlDocument, UrlDocumentModel.class);
    }

    private UrlDocument convert(final UrlDocumentModel urlDocumentModel) {
        return this.conversionService.convert(urlDocumentModel, UrlDocument.class);
    }
}
