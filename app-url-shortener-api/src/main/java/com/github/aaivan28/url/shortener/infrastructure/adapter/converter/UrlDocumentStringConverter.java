package com.github.aaivan28.url.shortener.infrastructure.adapter.converter;

import com.github.aaivan28.url.shortener.infrastructure.adapter.outbound.persistence.model.UrlDocument;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UrlDocumentStringConverter implements Converter<UrlDocument, String> {

    @Override
    public String convert(final @NonNull UrlDocument urlDocument) {
        return urlDocument.getUrl();
    }
}
