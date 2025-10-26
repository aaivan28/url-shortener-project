package com.github.aaivan28.url.shortener.admin.infrastructure.adapter.converter;

import com.github.aaivan28.url.shortener.admin.domain.model.CreateUrlDocumentModel;
import com.github.aaivan28.url.shortener.admin.infrastructure.adapter.inbound.rest.dto.CreateUrlDocumentRequestDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CreateUrlDocumentModelConverter implements Converter<CreateUrlDocumentRequestDTO, CreateUrlDocumentModel> {

    @Override
    public CreateUrlDocumentModel convert(final @NonNull CreateUrlDocumentRequestDTO createUrlDocumentRequest) {
        return CreateUrlDocumentModel.builder()
                .url(createUrlDocumentRequest.url())
                .description(createUrlDocumentRequest.description())
                .enabled(createUrlDocumentRequest.enabled())
                .build();
    }
}
