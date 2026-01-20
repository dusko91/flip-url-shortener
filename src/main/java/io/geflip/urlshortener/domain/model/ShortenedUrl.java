package io.geflip.urlshortener.domain.model;

import io.geflip.urlshortener.domain.model.value.OriginalUrl;
import io.geflip.urlshortener.domain.model.value.ShortCode;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
public class ShortenedUrl {
  private final ShortCode shortCode;
  private final OriginalUrl originalUrl;
}
