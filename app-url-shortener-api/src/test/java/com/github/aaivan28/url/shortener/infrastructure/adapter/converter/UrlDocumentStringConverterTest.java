package com.github.aaivan28.url.shortener.infrastructure.adapter.converter;

import com.github.aaivan28.url.shortener.infrastructure.adapter.outbound.persistence.UrlDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UrlDocumentStringConverter Tests")
class UrlDocumentStringConverterTest {

    private UrlDocumentStringConverter converter;

    @BeforeEach
    void setUp() {
        converter = new UrlDocumentStringConverter();
    }

    @Test
    @DisplayName("Should convert UrlDocument to URL string")
    void shouldConvertUrlDocumentToUrlString() {
        // Given
        final String expectedUrl = "https://example.com";
        final UrlDocument document = UrlDocument.builder()
                .id("id123")
                .key("abc123")
                .url(expectedUrl)
                .description("Test URL")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        final String actualUrl = converter.convert(document);

        // Then
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("Should extract URL with special characters")
    void shouldExtractUrlWithSpecialCharacters() {
        // Given
        final String expectedUrl = "https://example.com/path?param=value&other=123#section";
        final UrlDocument document = UrlDocument.builder()
                .id("id123")
                .key("special")
                .url(expectedUrl)
                .description("Special URL")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        final String actualUrl = converter.convert(document);

        // Then
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("Should extract URL with unicode characters")
    void shouldExtractUrlWithUnicodeCharacters() {
        // Given
        final String expectedUrl = "https://example.com/página/español";
        final UrlDocument document = UrlDocument.builder()
                .id("id123")
                .key("unicode")
                .url(expectedUrl)
                .description("Unicode URL")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        final String actualUrl = converter.convert(document);

        // Then
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("Should extract URL from disabled document")
    void shouldExtractUrlFromDisabledDocument() {
        // Given
        final String expectedUrl = "https://example.com";
        final UrlDocument document = UrlDocument.builder()
                .id("id123")
                .key("disabled")
                .url(expectedUrl)
                .description("Disabled URL")
                .enabled(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        final String actualUrl = converter.convert(document);

        // Then
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("Should extract URL from document with null description")
    void shouldExtractUrlFromDocumentWithNullDescription() {
        // Given
        final String expectedUrl = "https://example.com";
        final UrlDocument document = UrlDocument.builder()
                .id("id123")
                .key("nulldesc")
                .url(expectedUrl)
                .description(null)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        final String actualUrl = converter.convert(document);

        // Then
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("Should extract URL with port")
    void shouldExtractUrlWithPort() {
        // Given
        final String expectedUrl = "https://example.com:8080/path";
        final UrlDocument document = UrlDocument.builder()
                .id("id123")
                .key("port")
                .url(expectedUrl)
                .description("URL with port")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        final String actualUrl = converter.convert(document);

        // Then
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("Should extract localhost URL")
    void shouldExtractLocalhostUrl() {
        // Given
        final String expectedUrl = "http://localhost:3000";
        final UrlDocument document = UrlDocument.builder()
                .id("id123")
                .key("localhost")
                .url(expectedUrl)
                .description("Localhost URL")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        final String actualUrl = converter.convert(document);

        // Then
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("Should extract very long URL")
    void shouldExtractVeryLongUrl() {
        // Given
        final String expectedUrl = "https://example.com/very/long/path/" + "segment/".repeat(50) + "?param=value";
        final UrlDocument document = UrlDocument.builder()
                .id("id123")
                .key("longurl")
                .url(expectedUrl)
                .description("Very long URL")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        final String actualUrl = converter.convert(document);

        // Then
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("Should extract URL from recently created document")
    void shouldExtractUrlFromRecentlyCreatedDocument() {
        // Given
        final String expectedUrl = "https://example.com";
        final LocalDateTime now = LocalDateTime.now();
        final UrlDocument document = UrlDocument.builder()
                .id("id123")
                .key("recent")
                .url(expectedUrl)
                .description("Recent URL")
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // When
        final String actualUrl = converter.convert(document);

        // Then
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }
}