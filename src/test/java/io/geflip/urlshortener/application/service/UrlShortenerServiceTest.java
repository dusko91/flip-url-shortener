package io.geflip.urlshortener.application.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import io.geflip.urlshortener.domain.model.ShortenedUrl;
import io.geflip.urlshortener.domain.model.value.OriginalUrl;
import io.geflip.urlshortener.domain.model.value.ShortCode;
import io.geflip.urlshortener.domain.model.value.ShortUrl;
import io.geflip.urlshortener.domain.ports.out.persistence.ShortenedUrlRepositoryPort;
import io.geflip.urlshortener.infrastructure.adapters.out.generator.RandomShortCodeGenerator;
import io.geflip.urlshortener.infrastructure.configuration.UrlShortenerProperties;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceTest {

  @Mock private ShortenedUrlRepositoryPort urlRepository;
  @Mock private RandomShortCodeGenerator randomGenerator;
  @Mock private UrlShortenerProperties properties;

  @InjectMocks private UrlShortenerService service;

  private final String BASE_URL = "https://short.en";
  private final OriginalUrl ORIGINAL_URL = OriginalUrl.of("https://example.com");
  private final ShortCode EXISTING_CODE = ShortCode.of("abc123");
  private final ShortCode NEW_CODE = ShortCode.of("xyz789");

  @Test
  @DisplayName("Should return original URL when short URL exists")
  void testShortenUrl_existingUrl() {
    // given
    ShortenedUrl existingEntry = new ShortenedUrl(EXISTING_CODE, ORIGINAL_URL);
    when(properties.getBaseUrl()).thenReturn(BASE_URL);
    when(urlRepository.findByOriginalUrl(ORIGINAL_URL)).thenReturn(Optional.of(existingEntry));

    // when
    ShortUrl result = service.shortenUrl(ORIGINAL_URL);

    // then
    assertEquals(BASE_URL + "/" + EXISTING_CODE, result.value());
  }

  @Test
  @DisplayName("Should return new short URL when short URL does not exist")
  void testShortenUrl_newUrl() {
    // given
    when(properties.getBaseUrl()).thenReturn(BASE_URL);
    when(urlRepository.findByOriginalUrl(ORIGINAL_URL)).thenReturn(Optional.empty());
    when(randomGenerator.generateShortCode()).thenReturn(NEW_CODE);
    when(urlRepository.existsByShortCode(NEW_CODE)).thenReturn(false);

    // when
    ShortUrl result = service.shortenUrl(ORIGINAL_URL);

    // then
    assertEquals(BASE_URL + "/" + NEW_CODE, result.value());
    verify(urlRepository).save(any(ShortenedUrl.class));
  }

  @Test
  @DisplayName("Should retry generation if short code already exists")
  void testShortenUrl_shouldRetryGenerationIfShortCodeExists() {
    // given
    ShortCode code1 = ShortCode.of("dup123");
    ShortCode code2 = ShortCode.of("unique123");

    when(urlRepository.findByOriginalUrl(ORIGINAL_URL)).thenReturn(Optional.empty());
    when(properties.getBaseUrl()).thenReturn("https://short.ly");

    // First call returns code1 (which exists), second call returns code2 (unique)
    when(randomGenerator.generateShortCode()).thenReturn(code1, code2);

    // Mock the exists check: true for the first code, false for the second
    when(urlRepository.existsByShortCode(code1)).thenReturn(true);
    when(urlRepository.existsByShortCode(code2)).thenReturn(false);

    // when
    ShortUrl result = service.shortenUrl(ORIGINAL_URL);

    // then
    assertThat(result.value()).isEqualTo("https://short.ly/unique123");

    // Verify the generator was called exactly twice
    verify(randomGenerator, times(2)).generateShortCode();
    verify(urlRepository).save(any(ShortenedUrl.class));
  }
}
