package com.github.aaivan28.url.shortener.infrastructure.adapter.outbound.persistence;

import com.github.aaivan28.url.shortener.infrastructure.adapter.outbound.persistence.model.UrlDocument;
import com.github.aaivan28.url.shortener.infrastructure.adapter.outbound.persistence.repository.MongoRepositoryAdapter;
import com.github.aaivan28.url.shortener.infrastructure.adapter.outbound.persistence.repository.UrlMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("MongoRepositoryAdapter Tests")
class MongoRepositoryAdapterTest {

    @Mock
    private ConversionService conversionService;

    @Mock
    private UrlMongoRepository urlMongoRepository;

    private MongoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new MongoRepositoryAdapter(conversionService, urlMongoRepository);
    }

    @Test
    @DisplayName("Should return URL when document exists and is enabled")
    void shouldReturnUrlWhenDocumentExistsAndIsEnabled() {
        // Given
        final String urlKey = "abc123";
        final String expectedUrl = "https://example.com";
        final UrlDocument document = UrlDocument.builder()
                .id("id123")
                .key(urlKey)
                .url("https://example.com")
                .description("Test URL")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(urlMongoRepository.findByKey(urlKey)).thenReturn(Optional.of(document));
        when(conversionService.convert(document, String.class)).thenReturn(expectedUrl);

        // When
        final Optional<String> result = adapter.getUrl(urlKey);

        // Then
        assertThat(result).isPresent().contains(expectedUrl);
        verify(urlMongoRepository).findByKey(urlKey);
        verify(conversionService).convert(document, String.class);
    }

    @Test
    @DisplayName("Should return empty when document does not exist")
    void shouldReturnEmptyWhenDocumentDoesNotExist() {
        // Given
        final String urlKey = "nonexistent";
        when(urlMongoRepository.findByKey(urlKey)).thenReturn(Optional.empty());

        // When
        final Optional<String> result = adapter.getUrl(urlKey);

        // Then
        assertThat(result).isEmpty();
        verify(urlMongoRepository).findByKey(urlKey);
    }

    @Test
    @DisplayName("Should return empty when document is disabled")
    void shouldReturnEmptyWhenDocumentIsDisabled() {
        // Given
        final String urlKey = "disabled123";
        final UrlDocument document = UrlDocument.builder()
                .id("id123")
                .key(urlKey)
                .url("https://example.com")
                .description("Disabled URL")
                .enabled(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(urlMongoRepository.findByKey(urlKey)).thenReturn(Optional.of(document));

        // When
        final Optional<String> result = adapter.getUrl(urlKey);

        // Then
        assertThat(result).isEmpty();
        verify(urlMongoRepository).findByKey(urlKey);
    }

    @Test
    @DisplayName("Should handle URL with special characters")
    void shouldHandleUrlWithSpecialCharacters() {
        // Given
        final String urlKey = "special";
        final String expectedUrl = "https://example.com/path?param=value&other=123";
        final UrlDocument document = UrlDocument.builder()
                .id("id123")
                .key(urlKey)
                .url("https://example.com/path?param=value&other=123")
                .description("Special URL")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(urlMongoRepository.findByKey(urlKey)).thenReturn(Optional.of(document));
        when(conversionService.convert(document, String.class)).thenReturn(expectedUrl);

        // When
        final Optional<String> result = adapter.getUrl(urlKey);

        // Then
        assertThat(result).isPresent().contains(expectedUrl);
        verify(urlMongoRepository).findByKey(urlKey);
        verify(conversionService).convert(document, String.class);
    }

    @Test
    @DisplayName("Should handle document with null description")
    void shouldHandleDocumentWithNullDescription() {
        // Given
        final String urlKey = "nulldesc";
        final String expectedUrl = "https://example.com";
        final UrlDocument document = UrlDocument.builder()
                .id("id123")
                .key(urlKey)
                .url("https://example.com")
                .description(null)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(urlMongoRepository.findByKey(urlKey)).thenReturn(Optional.of(document));
        when(conversionService.convert(document, String.class)).thenReturn(expectedUrl);

        // When
        final Optional<String> result = adapter.getUrl(urlKey);

        // Then
        assertThat(result).isPresent().contains(expectedUrl);
        verify(urlMongoRepository).findByKey(urlKey);
        verify(conversionService).convert(document, String.class);
    }

    @Test
    @DisplayName("Should handle recently created document")
    void shouldHandleRecentlyCreatedDocument() {
        // Given
        final String urlKey = "new123";
        final String expectedUrl = "https://example.com";
        final LocalDateTime now = LocalDateTime.now();
        final UrlDocument document = UrlDocument.builder()
                .id("id123")
                .key(urlKey)
                .url("https://example.com")
                .description("New URL")
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(urlMongoRepository.findByKey(urlKey)).thenReturn(Optional.of(document));
        when(conversionService.convert(document, String.class)).thenReturn(expectedUrl);

        // When
        final Optional<String> result = adapter.getUrl(urlKey);

        // Then
        assertThat(result).isPresent().contains(expectedUrl);
        verify(urlMongoRepository).findByKey(urlKey);
        verify(conversionService).convert(document, String.class);
    }
}