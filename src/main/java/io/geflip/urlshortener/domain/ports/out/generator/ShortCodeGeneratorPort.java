package io.geflip.urlshortener.domain.ports.out.generator;

import io.geflip.urlshortener.domain.model.value.ShortCode;

/** Port for generating short codes. */
public interface ShortCodeGeneratorPort {
  /** Generates a short code. */
  ShortCode generateShortCode();
}
