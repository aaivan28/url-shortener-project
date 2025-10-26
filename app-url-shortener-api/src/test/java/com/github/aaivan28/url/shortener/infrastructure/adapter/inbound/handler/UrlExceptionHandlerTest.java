package com.github.aaivan28.url.shortener.infrastructure.adapter.inbound.handler;

import com.github.aaivan28.url.shortener.application.exception.UrlNotFoundException;
import com.github.aaivan28.url.shortener.domain.port.inbound.properties.RedirectBaseUrlProperty;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UrlExceptionHandler Tests")
class UrlExceptionHandlerTest {

    @Mock
    private RedirectBaseUrlProperty redirectBaseUrlProperty;

    @Mock
    private HttpServletResponse response;

    private UrlExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new UrlExceptionHandler(redirectBaseUrlProperty);
    }

    @Test
    @DisplayName("Should redirect to base URL when UrlNotFoundException is thrown")
    void shouldRedirectToBaseUrlWhenUrlNotFoundExceptionIsThrown() throws IOException {
        // Given
        final String baseUrl = "https://example.com";
        final UrlNotFoundException exception = new UrlNotFoundException("Key not found: abc123");
        when(redirectBaseUrlProperty.redirectBaseUrl()).thenReturn(baseUrl);

        // When
        handler.KeyNotFoundExceptionHandler(response, exception);

        // Then
        verify(redirectBaseUrlProperty).redirectBaseUrl();
        verify(response).sendRedirect(baseUrl);
    }

    @Test
    @DisplayName("Should handle base URL with path")
    void shouldHandleBaseUrlWithPath() throws IOException {
        // Given
        final String baseUrl = "https://example.com/home";
        final UrlNotFoundException exception = new UrlNotFoundException("Key not found: xyz789");
        when(redirectBaseUrlProperty.redirectBaseUrl()).thenReturn(baseUrl);

        // When
        handler.KeyNotFoundExceptionHandler(response, exception);

        // Then
        verify(redirectBaseUrlProperty).redirectBaseUrl();
        verify(response).sendRedirect(baseUrl);
    }

    @Test
    @DisplayName("Should handle base URL with query parameters")
    void shouldHandleBaseUrlWithQueryParameters() throws IOException {
        // Given
        final String baseUrl = "https://example.com?error=not_found";
        final UrlNotFoundException exception = new UrlNotFoundException("Key not found: test123");
        when(redirectBaseUrlProperty.redirectBaseUrl()).thenReturn(baseUrl);

        // When
        handler.KeyNotFoundExceptionHandler(response, exception);

        // Then
        verify(redirectBaseUrlProperty).redirectBaseUrl();
        verify(response).sendRedirect(baseUrl);
    }

    @Test
    @DisplayName("Should handle exception with empty message")
    void shouldHandleExceptionWithEmptyMessage() throws IOException {
        // Given
        final String baseUrl = "https://example.com";
        final UrlNotFoundException exception = new UrlNotFoundException("");
        when(redirectBaseUrlProperty.redirectBaseUrl()).thenReturn(baseUrl);

        // When
        handler.KeyNotFoundExceptionHandler(response, exception);

        // Then
        verify(redirectBaseUrlProperty).redirectBaseUrl();
        verify(response).sendRedirect(baseUrl);
    }

    @Test
    @DisplayName("Should handle exception with null message")
    void shouldHandleExceptionWithNullMessage() throws IOException {
        // Given
        final String baseUrl = "https://example.com";
        final UrlNotFoundException exception = new UrlNotFoundException(null);
        when(redirectBaseUrlProperty.redirectBaseUrl()).thenReturn(baseUrl);

        // When
        handler.KeyNotFoundExceptionHandler(response, exception);

        // Then
        verify(redirectBaseUrlProperty).redirectBaseUrl();
        verify(response).sendRedirect(baseUrl);
    }

    @Test
    @DisplayName("Should handle exception with long message")
    void shouldHandleExceptionWithLongMessage() throws IOException {
        // Given
        final String baseUrl = "https://example.com";
        final String longMessage = "Key not found: " + "a".repeat(1000);
        final UrlNotFoundException exception = new UrlNotFoundException(longMessage);
        when(redirectBaseUrlProperty.redirectBaseUrl()).thenReturn(baseUrl);

        // When
        handler.KeyNotFoundExceptionHandler(response, exception);

        // Then
        verify(redirectBaseUrlProperty).redirectBaseUrl();
        verify(response).sendRedirect(baseUrl);
    }
}