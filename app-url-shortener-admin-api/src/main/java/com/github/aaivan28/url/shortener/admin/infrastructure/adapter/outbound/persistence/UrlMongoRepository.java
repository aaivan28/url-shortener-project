package com.github.aaivan28.url.shortener.admin.infrastructure.adapter.outbound.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlMongoRepository extends CrudRepository<UrlDocument, String> {
}
