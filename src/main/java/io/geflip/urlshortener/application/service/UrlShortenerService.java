package io.geflip.urlshortener.application.service;

import io.geflip.urlshortener.domain.model.ShortenedUrl;
import io.geflip.urlshortener.domain.model.value.OriginalUrl;
import io.geflip.urlshortener.domain.model.value.ShortCode;
import io.geflip.urlshortener.domain.model.value.ShortUrl;
import io.geflip.urlshortener.domain.ports.in.url.UrlShortenerPort;
import io.geflip.urlshortener.domain.ports.out.persistence.ShortenedUrlRepositoryPort;
import io.geflip.urlshortener.infrastructure.adapters.out.generator.RandomShortCodeGenerator;
import io.geflip.urlshortener.infrastructure.configuration.UrlShortenerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlShortenerService implements UrlShortenerPort {

  private final UrlShortenerProperties urlShortenerProperties;
  private final ShortenedUrlRepositoryPort urlRepository;
  private final RandomShortCodeGenerator randomShortCodeGenerator;

  @Override
  public ShortUrl shortenUrl(final OriginalUrl originalUrl) {
    final ShortCode shortCode =
        urlRepository
            .findByOriginalUrl(originalUrl)
            .map(ShortenedUrl::getShortCode)
            .orElseGet(() -> createNewShortCode(originalUrl));

    return getShortUrl(shortCode);
  }

  private ShortCode createNewShortCode(final OriginalUrl originalUrl) {
    final ShortenedUrl newShortenedUrl =
        ShortenedUrl.builder()
            .shortCode(createNewRandomShortCode())
            .originalUrl(originalUrl)
            .build();

    urlRepository.save(newShortenedUrl);
    return newShortenedUrl.getShortCode();
  }

  private ShortUrl getShortUrl(final ShortCode shortCode) {
    return ShortUrl.of(urlShortenerProperties.getBaseUrl() + "/" + shortCode);
  }

  private ShortCode createNewRandomShortCode() {
    ShortCode shortCode;
    do {
      shortCode = randomShortCodeGenerator.generateShortCode();
    } while (urlRepository.existsByShortCode(shortCode));
    return shortCode;
  }
}
