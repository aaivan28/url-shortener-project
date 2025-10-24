package com.github.aaivan28.url.shortener.infrastructure.adapter.inbound.rest;

import com.github.aaivan28.url.shortener.domain.port.inbound.usescases.UrlUsesCases;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.aop.MeterTag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlUsesCases urlUsesCases;

    @GetMapping("/{urlKey}")
    @Timed(value = "url.shortener.api.controller.redirect", description = "Redirect to the original URL")
    public void redirect(final HttpServletResponse response, final @MeterTag(key = "key") @PathVariable("urlKey") String urlKey) throws IOException {
        response.sendRedirect(this.urlUsesCases.getUrl(urlKey));
    }
}
