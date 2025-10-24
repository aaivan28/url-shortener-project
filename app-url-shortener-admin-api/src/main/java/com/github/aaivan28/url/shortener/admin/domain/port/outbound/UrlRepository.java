package com.github.aaivan28.url.shortener.admin.domain.port.outbound;

import com.github.aaivan28.url.shortener.admin.domain.model.UrlDocument;
import org.springframework.data.domain.Page;

public interface UrlRepository {

    Page<UrlDocument> getRecords();
}
