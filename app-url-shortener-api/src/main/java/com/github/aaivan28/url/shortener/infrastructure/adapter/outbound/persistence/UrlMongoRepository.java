package com.github.aaivan28.url.shortener.infrastructure.adapter.outbound.persistence;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlMongoRepository extends CrudRepository<UrlDocument, String> {

    @Cacheable(value = "url", key = "#key")
    Optional<UrlDocument> findByKey(final String key);
}
