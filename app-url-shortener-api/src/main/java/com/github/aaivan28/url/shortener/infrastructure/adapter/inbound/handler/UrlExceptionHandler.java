package com.github.aaivan28.url.shortener.infrastructure.adapter.inbound.handler;

import com.github.aaivan28.url.shortener.application.exception.UrlNotFoundException;
import com.github.aaivan28.url.shortener.domain.port.inbound.properties.RedirectBaseUrlProperty;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class UrlExceptionHandler {

    private final RedirectBaseUrlProperty redirectBaseUrlProperty;

    @ExceptionHandler({UrlNotFoundException.class})
    public void KeyNotFoundExceptionHandler(final HttpServletResponse response, final UrlNotFoundException exception) throws IOException {
        log.debug("Key not found: {}", exception.getMessage());
        response.sendRedirect(this.redirectBaseUrlProperty.redirectBaseUrl());
    }
}
