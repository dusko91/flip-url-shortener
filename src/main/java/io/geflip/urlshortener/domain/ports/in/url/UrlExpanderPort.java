package io.geflip.urlshortener.domain.ports.in.url;

import io.geflip.urlshortener.domain.model.value.OriginalUrl;
import io.geflip.urlshortener.domain.model.value.ShortUrl;
import java.util.Optional;

/** Port for expanding short URLs. */
public interface UrlExpanderPort {
  /** Returns the original URL for the given short URL. */
  Optional<OriginalUrl> getOriginalUrl(ShortUrl shortUrl);
}
