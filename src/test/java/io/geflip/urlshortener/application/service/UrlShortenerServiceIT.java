package io.geflip.urlshortener.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import io.geflip.urlshortener.domain.model.value.OriginalUrl;
import io.geflip.urlshortener.domain.model.value.ShortUrl;
import io.geflip.urlshortener.domain.ports.in.url.UrlShortenerPort;
import io.geflip.urlshortener.infrastructure.adapters.out.persistence.JpaShortenedUrlRepository;
import io.geflip.urlshortener.infrastructure.adapters.out.persistence.entity.ShortenedUrlEntity;
import io.geflip.urlshortener.infrastructure.configuration.UrlShortenerProperties;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UrlShortenerServiceIT {

  @Autowired private UrlShortenerPort urlShortenerPort;
  @Autowired private JpaShortenedUrlRepository urlRepository;
  @Autowired private UrlShortenerProperties urlShortenerProperties;

  @BeforeEach
  void setUp() {
    urlRepository.deleteAll();
  }

  @Test
  @DisplayName("Should generate different short codes for different URLs")
  void testShortenDifferentUrls() {
    // given
    OriginalUrl url1 = OriginalUrl.of("https://example.com/page1");
    OriginalUrl url2 = OriginalUrl.of("https://example.com/page2");
    OriginalUrl url3 = OriginalUrl.of("https://example.com/page3");

    // when
    ShortUrl short1 = urlShortenerPort.shortenUrl(url1);
    ShortUrl short2 = urlShortenerPort.shortenUrl(url2);
    ShortUrl short3 = urlShortenerPort.shortenUrl(url3);

    // then - all short URLs should be different
    assertThat(short1.value()).isNotEqualTo(short2.value());
    assertThat(short1.value()).isNotEqualTo(short3.value());
    assertThat(short2.value()).isNotEqualTo(short3.value());

    // Verify all were saved
    assertThat(urlRepository.findAll()).hasSize(3);
  }

  @Test
  @DisplayName("Should shorten a new URL and save it to database")
  void testShortenNewUrl() {
    // given
    OriginalUrl originalUrl = OriginalUrl.of("https://example.com/very/long/path");

    // when
    ShortUrl result = urlShortenerPort.shortenUrl(originalUrl);

    // then
    assertThat(result).isNotNull();
    assertThat(result.value()).startsWith(urlShortenerProperties.getBaseUrl());

    // Verify it was saved to database
    Optional<ShortenedUrlEntity> saved = urlRepository.findByOriginalUrl(originalUrl.value());
    assertThat(saved).isPresent();
    assertThat(saved.get().getOriginalUrl()).isEqualTo(originalUrl.value());
  }

  @Test
  @DisplayName("Should return existing short URL when original URL already exists")
  void testShortenExistingUrl() {
    // given
    OriginalUrl originalUrl = OriginalUrl.of("https://example.com/existing");

    // when
    ShortUrl result1 = urlShortenerPort.shortenUrl(originalUrl);
    ShortUrl result2 = urlShortenerPort.shortenUrl(originalUrl);

    // then - should return the same short URL both times
    assertThat(result1.value()).isEqualTo(result2.value());

    // Verify only one entry exists in database
    List<ShortenedUrlEntity> allUrls = urlRepository.findAll();
    assertThat(allUrls).hasSize(1);
  }
}
