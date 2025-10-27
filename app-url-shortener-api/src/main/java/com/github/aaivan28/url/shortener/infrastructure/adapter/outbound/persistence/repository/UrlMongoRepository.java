package com.github.aaivan28.url.shortener.infrastructure.adapter.outbound.persistence.repository;

import com.github.aaivan28.url.shortener.infrastructure.adapter.outbound.persistence.model.UrlDocument;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlMongoRepository extends CrudRepository<UrlDocument, String> {

    @Cacheable(value = "document", key = "#key", unless = "#result == null")
    Optional<UrlDocument> findByKey(final String key);
}
