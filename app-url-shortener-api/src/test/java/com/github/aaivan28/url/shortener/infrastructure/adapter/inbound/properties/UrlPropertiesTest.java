package com.github.aaivan28.url.shortener.infrastructure.adapter.inbound.properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UrlProperties Tests")
class UrlPropertiesTest {

    @Test
    @DisplayName("Should create UrlProperties with redirect base URL")
    void shouldCreateUrlPropertiesWithRedirectBaseUrl() {
        // Given
        final String expectedUrl = "https://example.com";

        // When
        final UrlProperties properties = new UrlProperties(expectedUrl);

        // Then
        assertThat(properties.redirectBaseUrl()).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("Should implement RedirectBaseUrlProperty interface")
    void shouldImplementRedirectBaseUrlPropertyInterface() {
        // Given
        final String expectedUrl = "https://example.com/home";
        final UrlProperties properties = new UrlProperties(expectedUrl);

        // When
        final String actualUrl = properties.redirectBaseUrl();

        // Then
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("Should handle URL with path")
    void shouldHandleUrlWithPath() {
        // Given
        final String expectedUrl = "https://example.com/path/to/page";
        final UrlProperties properties = new UrlProperties(expectedUrl);

        // When
        final String actualUrl = properties.redirectBaseUrl();

        // Then
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("Should handle URL with query parameters")
    void shouldHandleUrlWithQueryParameters() {
        // Given
        final String expectedUrl = "https://example.com?param=value";
        final UrlProperties properties = new UrlProperties(expectedUrl);

        // When
        final String actualUrl = properties.redirectBaseUrl();

        // Then
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("Should handle URL with port")
    void shouldHandleUrlWithPort() {
        // Given
        final String expectedUrl = "https://example.com:8080";
        final UrlProperties properties = new UrlProperties(expectedUrl);

        // When
        final String actualUrl = properties.redirectBaseUrl();

        // Then
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("Should handle localhost URL")
    void shouldHandleLocalhostUrl() {
        // Given
        final String expectedUrl = "http://localhost:3000";
        final UrlProperties properties = new UrlProperties(expectedUrl);

        // When
        final String actualUrl = properties.redirectBaseUrl();

        // Then
        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("Should be equal when redirect URLs are the same")
    void shouldBeEqualWhenRedirectUrlsAreTheSame() {
        // Given
        final String url = "https://example.com";
        final UrlProperties properties1 = new UrlProperties(url);
        final UrlProperties properties2 = new UrlProperties(url);

        // Then
        assertThat(properties1).isEqualTo(properties2);
        assertThat(properties1.hashCode()).isEqualTo(properties2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when redirect URLs are different")
    void shouldNotBeEqualWhenRedirectUrlsAreDifferent() {
        // Given
        final UrlProperties properties1 = new UrlProperties("https://example.com");
        final UrlProperties properties2 = new UrlProperties("https://other.com");

        // Then
        assertThat(properties1).isNotEqualTo(properties2);
    }

    @Test
    @DisplayName("Should have meaningful toString")
    void shouldHaveMeaningfulToString() {
        // Given
        final String url = "https://example.com";
        final UrlProperties properties = new UrlProperties(url);

        // When
        final String toString = properties.toString();

        // Then
        assertThat(toString)
                .contains("UrlProperties")
                .contains(url);
    }
}