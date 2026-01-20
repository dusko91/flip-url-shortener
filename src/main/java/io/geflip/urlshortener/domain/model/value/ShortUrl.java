package io.geflip.urlshortener.domain.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

public record ShortUrl(String value) {
  @JsonCreator
  public ShortUrl {
    Objects.requireNonNull(value, "URL cannot be null");
    if (value.isBlank()) {
      throw new IllegalArgumentException("URL cannot be blank");
    }
    // TODO validate URL
  }

  public static ShortUrl of(final String value) {
    return new ShortUrl(value);
  }

  @JsonValue
  @Override
  public String toString() {
    return value;
  }
}
