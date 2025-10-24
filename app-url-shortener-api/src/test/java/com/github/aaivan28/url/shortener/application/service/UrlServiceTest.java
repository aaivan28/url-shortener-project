package com.github.aaivan28.url.shortener.application.service;

import com.github.aaivan28.url.shortener.application.exception.UrlNotFoundException;
import com.github.aaivan28.url.shortener.domain.port.outbound.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UrlService Tests")
class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    private UrlService urlService;

    @BeforeEach
    void setUp() {
        urlService = new UrlService(urlRepository);
    }

    @Test
    @DisplayName("Should return URL when key exists")
    void shouldReturnUrlWhenKeyExists() {
        // Given
        final String urlKey = "abc123";
        final String expectedUrl = "https://example.com";
        when(urlRepository.getUrl(urlKey)).thenReturn(Optional.of(expectedUrl));

        // When
        final String actualUrl = urlService.getUrl(urlKey);

        // Then
        assertThat(actualUrl).isEqualTo(expectedUrl);
        verify(urlRepository).getUrl(urlKey);
    }

    @Test
    @DisplayName("Should throw UrlNotFoundException when key does not exist")
    void shouldThrowExceptionWhenKeyDoesNotExist() {
        // Given
        final String urlKey = "nonexistent";
        when(urlRepository.getUrl(urlKey)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> urlService.getUrl(urlKey))
                .isInstanceOf(UrlNotFoundException.class)
                .hasMessage("Key not found: " + urlKey);
        verify(urlRepository).getUrl(urlKey);
    }

    @Test
    @DisplayName("Should handle URL with special characters")
    void shouldHandleUrlWithSpecialCharacters() {
        // Given
        final String urlKey = "special123";
        final String expectedUrl = "https://example.com/path?param=value&other=123";
        when(urlRepository.getUrl(urlKey)).thenReturn(Optional.of(expectedUrl));

        // When
        final String actualUrl = urlService.getUrl(urlKey);

        // Then
        assertThat(actualUrl).isEqualTo(expectedUrl);
        verify(urlRepository).getUrl(urlKey);
    }

    @Test
    @DisplayName("Should handle empty string key")
    void shouldHandleEmptyStringKey() {
        // Given
        final String urlKey = "";
        when(urlRepository.getUrl(urlKey)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> urlService.getUrl(urlKey))
                .isInstanceOf(UrlNotFoundException.class)
                .hasMessage("Key not found: ");
        verify(urlRepository).getUrl(urlKey);
    }
}
