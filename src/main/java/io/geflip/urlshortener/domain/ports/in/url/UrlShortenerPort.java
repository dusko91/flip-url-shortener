package io.geflip.urlshortener.domain.ports.in.url;

import io.geflip.urlshortener.domain.model.value.OriginalUrl;
import io.geflip.urlshortener.domain.model.value.ShortUrl;

/** Port for shortening URLs. */
public interface UrlShortenerPort {
  /** Shortens the given URL. */
  ShortUrl shortenUrl(OriginalUrl originalUrl);
}
