package io.geflip.urlshortener.application.service;

import io.geflip.urlshortener.domain.model.ShortenedUrl;
import io.geflip.urlshortener.domain.model.value.OriginalUrl;
import io.geflip.urlshortener.domain.model.value.ShortCode;
import io.geflip.urlshortener.domain.model.value.ShortUrl;
import io.geflip.urlshortener.domain.ports.in.url.UrlExpanderPort;
import io.geflip.urlshortener.domain.ports.out.persistence.ShortenedUrlRepositoryPort;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UrlExpanderService implements UrlExpanderPort {

  private final ShortenedUrlRepositoryPort urlRepository;

  @Override
  public Optional<OriginalUrl> getOriginalUrl(final ShortUrl shortUrl) {
    final ShortCode shortCode = getShortCode(shortUrl);
    return urlRepository.findByShortCode(shortCode).map(ShortenedUrl::getOriginalUrl);
  }

  private ShortCode getShortCode(final ShortUrl shortUrl) {
    final int lastSlashIndex = shortUrl.value().lastIndexOf('/');
    final String afterLastSlash = shortUrl.value().substring(lastSlashIndex + 1);
    return ShortCode.of(afterLastSlash);
  }
}
