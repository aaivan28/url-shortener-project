package com.github.aaivan28.url.shortener.infrastructure.adapter.inbound.rest;

import com.github.aaivan28.url.shortener.application.exception.UrlNotFoundException;
import com.github.aaivan28.url.shortener.domain.port.inbound.usescases.UrlUsesCases;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UrlShortenerController Tests")
class UrlShortenerControllerTest {

    @Mock
    private UrlUsesCases urlUsesCases;

    @Mock
    private HttpServletResponse response;

    private UrlShortenerController controller;

    @BeforeEach
    void setUp() {
        controller = new UrlShortenerController(urlUsesCases);
    }

    @Test
    @DisplayName("Should redirect to URL when key exists")
    void shouldRedirectToUrlWhenKeyExists() throws IOException {
        // Given
        final String urlKey = "abc123";
        final String targetUrl = "https://example.com";
        when(urlUsesCases.getUrl(urlKey)).thenReturn(targetUrl);

        // When
        controller.redirect(response, urlKey);

        // Then
        verify(urlUsesCases).getUrl(urlKey);
        verify(response).sendRedirect(targetUrl);
    }

    @Test
    @DisplayName("Should propagate exception when URL not found")
    void shouldPropagateExceptionWhenUrlNotFound() {
        // Given
        final String urlKey = "nonexistent";
        when(urlUsesCases.getUrl(urlKey)).thenThrow(new UrlNotFoundException("Key not found: " + urlKey));

        // When & Then
        assertThatThrownBy(() -> controller.redirect(response, urlKey))
                .isInstanceOf(UrlNotFoundException.class)
                .hasMessage("Key not found: " + urlKey);
        verify(urlUsesCases).getUrl(urlKey);
    }

    @Test
    @DisplayName("Should handle URL with special characters")
    void shouldHandleUrlWithSpecialCharacters() throws IOException {
        // Given
        final String urlKey = "special";
        final String targetUrl = "https://example.com/path?param=value&other=123#section";
        when(urlUsesCases.getUrl(urlKey)).thenReturn(targetUrl);

        // When
        controller.redirect(response, urlKey);

        // Then
        verify(urlUsesCases).getUrl(urlKey);
        verify(response).sendRedirect(targetUrl);
    }

    @Test
    @DisplayName("Should handle URL with unicode characters")
    void shouldHandleUrlWithUnicodeCharacters() throws IOException {
        // Given
        final String urlKey = "unicode";
        final String targetUrl = "https://example.com/página/español";
        when(urlUsesCases.getUrl(urlKey)).thenReturn(targetUrl);

        // When
        controller.redirect(response, urlKey);

        // Then
        verify(urlUsesCases).getUrl(urlKey);
        verify(response).sendRedirect(targetUrl);
    }

    @Test
    @DisplayName("Should handle short key")
    void shouldHandleShortKey() throws IOException {
        // Given
        final String urlKey = "a";
        final String targetUrl = "https://example.com";
        when(urlUsesCases.getUrl(urlKey)).thenReturn(targetUrl);

        // When
        controller.redirect(response, urlKey);

        // Then
        verify(urlUsesCases).getUrl(urlKey);
        verify(response).sendRedirect(targetUrl);
    }

    @Test
    @DisplayName("Should handle long key")
    void shouldHandleLongKey() throws IOException {
        // Given
        final String urlKey = "a".repeat(100);
        final String targetUrl = "https://example.com";
        when(urlUsesCases.getUrl(urlKey)).thenReturn(targetUrl);

        // When
        controller.redirect(response, urlKey);

        // Then
        verify(urlUsesCases).getUrl(urlKey);
        verify(response).sendRedirect(targetUrl);
    }
}