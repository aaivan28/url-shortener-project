package com.github.aaivan28.url.shortener.admin.infrastructure.adapter.inbound.rest.controller;

import com.github.aaivan28.url.shortener.admin.domain.model.CreateUrlDocumentModel;
import com.github.aaivan28.url.shortener.admin.domain.port.inbound.DeleteUrlUsesCases;
import com.github.aaivan28.url.shortener.admin.domain.port.inbound.ReadUrlUsesCases;
import com.github.aaivan28.url.shortener.admin.domain.port.inbound.WriteUrlUsesCases;
import com.github.aaivan28.url.shortener.admin.infrastructure.adapter.inbound.rest.dto.CreateUrlDocumentRequestDTO;
import com.github.aaivan28.url.shortener.admin.infrastructure.adapter.inbound.rest.dto.PaginateResponseDTO;
import com.github.aaivan28.url.shortener.admin.infrastructure.adapter.inbound.rest.dto.SearchUrlDocumentRequestDTO;
import com.github.aaivan28.url.shortener.admin.infrastructure.adapter.inbound.rest.dto.UrlDocumentDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class UrlShortenerAdminController {

    private final ReadUrlUsesCases readUrlUsesCases;
    private final WriteUrlUsesCases writeUrlUsesCases;
    private final DeleteUrlUsesCases deleteUrlUsesCases;
    private final ConversionService conversionService;

    @PostMapping("/document/search")
    public PaginateResponseDTO<UrlDocumentDTO> search(final SearchUrlDocumentRequestDTO request) {
        //this.readUrlUsesCases.searchUrl();
        return null;
    }

    @GetMapping("/document/{code}")
    public UrlDocumentDTO getByCode(final @PathVariable("code") String code) {
        return this.conversionService.convert(this.readUrlUsesCases.getByCode(code), UrlDocumentDTO.class);
    }

    @PostMapping("/document")
    public UrlDocumentDTO create(final @RequestBody @Valid CreateUrlDocumentRequestDTO request) {
        final CreateUrlDocumentModel createUrlDocumentModel = this.conversionService.convert(request, CreateUrlDocumentModel.class);
        return this.conversionService.convert(this.writeUrlUsesCases.createUrlDetail(createUrlDocumentModel), UrlDocumentDTO.class);
    }

    @DeleteMapping("/document/{code}")
    public void delete(final @PathVariable("code") String code) {
        this.deleteUrlUsesCases.deleteUrl(code);
    }
}
