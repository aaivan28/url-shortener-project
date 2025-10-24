package com.github.aaivan28.url.shortener.admin.infrastructure.adapter.inbound.rest;

import com.github.aaivan28.url.shortener.admin.application.UrlService;
import com.github.aaivan28.url.shortener.admin.infrastructure.adapter.model.PaginateResponse;
import com.github.aaivan28.url.shortener.admin.infrastructure.adapter.model.UrlDocumentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UrlShortenerAdminController {

    private final UrlService urlService;

    @PostMapping("/document/search")
    public PaginateResponse<UrlDocumentDTO> search(final Pageable pageable) {
        return null;
    }

    @GetMapping("/document/{key}")
    public UrlDocumentDTO getDetail(final @PathVariable("key") String key) {
        return null;
    }

    @DeleteMapping("/document/{key}")
    public void delete(final @PathVariable("key") String key) {

    }
}
