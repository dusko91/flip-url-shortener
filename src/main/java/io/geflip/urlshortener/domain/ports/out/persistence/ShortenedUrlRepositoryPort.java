package io.geflip.urlshortener.domain.ports.out.persistence;

import io.geflip.urlshortener.domain.model.ShortenedUrl;
import io.geflip.urlshortener.domain.model.value.OriginalUrl;
import io.geflip.urlshortener.domain.model.value.ShortCode;
import java.util.Optional;

/** Port for accessing the shortened URL repository. */
public interface ShortenedUrlRepositoryPort {
  /** Returns true if the short code already exists. */
  boolean existsByShortCode(ShortCode shortCode);

  /** Returns the shortened URL for the given short code. */
  Optional<ShortenedUrl> findByShortCode(ShortCode shortCode);

  /** Returns the original URL for the given original URL, if it exists. */
  Optional<ShortenedUrl> findByOriginalUrl(OriginalUrl originalUrl);

  /** Saves the given shortened URL. */
  ShortenedUrl save(ShortenedUrl shortenedUrl);
}
