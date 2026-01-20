package io.geflip.urlshortener.application.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.geflip.urlshortener.domain.model.ShortenedUrl;
import io.geflip.urlshortener.domain.model.value.OriginalUrl;
import io.geflip.urlshortener.domain.model.value.ShortCode;
import io.geflip.urlshortener.domain.model.value.ShortUrl;
import io.geflip.urlshortener.domain.ports.out.persistence.ShortenedUrlRepositoryPort;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("UrlExpanderService")
class UrlExpanderServiceTest {

  @Mock private ShortenedUrlRepositoryPort repository;

  @InjectMocks private UrlExpanderService service;

  @Test
  @DisplayName("Should extract short code from shortUrl and return originalUrl")
  void testGetOriginalUrl_extractsShortCodeAndReturnOriginalUrl() {
    // given
    String baseUrl = "https://short.ly/";
    String shortUrlValue = "https://short.ly/abc123";
    ShortCode expectedCode = ShortCode.of("abc123");
    OriginalUrl expectedOriginal = OriginalUrl.of("https://example.com");

    when(repository.findByShortCode(expectedCode))
        .thenReturn(Optional.of(new ShortenedUrl(expectedCode, expectedOriginal)));

    // when
    Optional<OriginalUrl> result = service.getOriginalUrl(ShortUrl.of(shortUrlValue));

    // then
    assertThat(result).isPresent().contains(expectedOriginal);
    verify(repository).findByShortCode(expectedCode);
  }
}
