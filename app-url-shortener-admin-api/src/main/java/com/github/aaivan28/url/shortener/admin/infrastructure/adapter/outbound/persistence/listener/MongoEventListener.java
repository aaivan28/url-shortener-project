package com.github.aaivan28.url.shortener.admin.infrastructure.adapter.outbound.persistence.listener;

import com.github.aaivan28.url.shortener.admin.infrastructure.adapter.outbound.persistence.model.UrlDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MongoEventListener extends AbstractMongoEventListener<UrlDocument> {

    private static final String CACHE_NAME = "document";

    private final CacheManager cacheManager;

    @Override
    public void onAfterDelete(final @NonNull AfterDeleteEvent<UrlDocument> event) {
        final Cache cache = this.cacheManager.getCache(CACHE_NAME);
        final String key = event.getSource().getString(UrlDocument.KEY_FIELD);
        if (cache != null && key != null) {
            cache.evict(key);
            log.debug("Remove from cache: {}", key);
        }
        super.onAfterDelete(event);
    }
}
