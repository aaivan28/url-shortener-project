package com.github.aaivan28.url.shortener.admin.infrastructure.adapter.outbound.persistence;

import com.github.aaivan28.url.shortener.admin.domain.model.UrlDocument;
import com.github.aaivan28.url.shortener.admin.domain.port.outbound.UrlRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class MongoRepositoryAdapter implements UrlRepository {

    @Override
    public Page<UrlDocument> getRecords() {
        return null;
    }
}
