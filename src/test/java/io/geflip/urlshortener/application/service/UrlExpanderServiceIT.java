package io.geflip.urlshortener.application.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import io.geflip.urlshortener.domain.model.value.OriginalUrl;
import io.geflip.urlshortener.domain.model.value.ShortUrl;
import io.geflip.urlshortener.domain.ports.in.url.UrlExpanderPort;
import io.geflip.urlshortener.infrastructure.adapters.out.persistence.JpaShortenedUrlRepository;
import io.geflip.urlshortener.infrastructure.adapters.out.persistence.entity.ShortenedUrlEntity;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UrlExpanderServiceIT {
  @Autowired private UrlExpanderPort urlExpanderPort;
  @Autowired private JpaShortenedUrlRepository urlRepository;

  @BeforeEach
  void setUp() {
    urlRepository.deleteAll();
  }

  @Test
  @DisplayName("Should return empty Optional when short URL does not exist")
  void testGetOriginal_UrlNotFound() {
    // given
    ShortUrl shortUrl = ShortUrl.of("https://sh.url.io/nonexistent");

    // when
    Optional<OriginalUrl> result = urlExpanderPort.getOriginalUrl(shortUrl);

    // then
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("Should return original URL when short URL exists in database")
  void testGetOriginalUrlFound() {
    // given
    String originalUrlValue = "https://example.com/very/long/path";
    String shortUrlValue = "https://sh.url.io/abc123";
    String shortCode = "abc123";

    // Save URL mapping to database
    ShortenedUrlEntity urlEntity = new ShortenedUrlEntity();
    urlEntity.setShortCode(shortCode);
    urlEntity.setOriginalUrl(originalUrlValue);
    urlRepository.save(urlEntity);

    // when
    ShortUrl shortUrl = ShortUrl.of(shortUrlValue);
    Optional<OriginalUrl> result = urlExpanderPort.getOriginalUrl(shortUrl);

    // then
    assertThat(result).isPresent();
    assertThat(result.get().value()).isEqualTo(originalUrlValue);
  }
}
