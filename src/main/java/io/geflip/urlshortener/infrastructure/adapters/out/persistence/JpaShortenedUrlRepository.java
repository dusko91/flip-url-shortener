package io.geflip.urlshortener.infrastructure.adapters.out.persistence;

import io.geflip.urlshortener.infrastructure.adapters.out.persistence.entity.ShortenedUrlEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaShortenedUrlRepository extends JpaRepository<ShortenedUrlEntity, Long> {
  Optional<ShortenedUrlEntity> findByShortCode(String shortCode);

  boolean existsByShortCode(String shortCode);

  Optional<ShortenedUrlEntity> findByOriginalUrl(String originalUrl);
}
