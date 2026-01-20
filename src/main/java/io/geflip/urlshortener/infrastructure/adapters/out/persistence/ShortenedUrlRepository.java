package io.geflip.urlshortener.infrastructure.adapters.out.persistence;

import io.geflip.urlshortener.domain.model.ShortenedUrl;
import io.geflip.urlshortener.domain.model.value.OriginalUrl;
import io.geflip.urlshortener.domain.model.value.ShortCode;
import io.geflip.urlshortener.domain.ports.out.persistence.ShortenedUrlRepositoryPort;
import io.geflip.urlshortener.infrastructure.adapters.out.persistence.entity.ShortenedUrlEntity;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class ShortenedUrlRepository implements ShortenedUrlRepositoryPort {
  private final JpaShortenedUrlRepository jpaRepository;

  public boolean existsByShortCode(final ShortCode shortCode) {
    return jpaRepository.existsByShortCode(shortCode.value());
  }

  public Optional<ShortenedUrl> findByShortCode(final ShortCode shortCode) {
    return jpaRepository.findByShortCode(shortCode.value()).map(this::toDomain);
  }

  public Optional<ShortenedUrl> findByOriginalUrl(final OriginalUrl originalUrl) {
    return jpaRepository.findByOriginalUrl(originalUrl.value()).map(this::toDomain);
  }

  @Override
  public ShortenedUrl save(ShortenedUrl shortenedUrl) {
    final ShortenedUrlEntity entity = new ShortenedUrlEntity();
    entity.setOriginalUrl(shortenedUrl.getOriginalUrl().value());
    entity.setShortCode(shortenedUrl.getShortCode().value());
    return toDomain(jpaRepository.save(entity));
  }

  private ShortenedUrl toDomain(final ShortenedUrlEntity entity) {
    return ShortenedUrl.builder()
        .originalUrl(new OriginalUrl(entity.getOriginalUrl()))
        .shortCode(new ShortCode(entity.getShortCode()))
        .build();
  }
}
