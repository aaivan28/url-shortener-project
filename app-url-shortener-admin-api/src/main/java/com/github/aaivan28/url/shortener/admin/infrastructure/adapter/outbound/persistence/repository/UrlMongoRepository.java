package com.github.aaivan28.url.shortener.admin.infrastructure.adapter.outbound.persistence.repository;

import com.github.aaivan28.url.shortener.admin.infrastructure.adapter.outbound.persistence.model.UrlDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlMongoRepository extends CrudRepository<UrlDocument, String> {
    Page<UrlDocument> findAll(final Pageable pageable);
    Optional<UrlDocument> findByKey(final String key);
    boolean existsByKey(final String key);
}
