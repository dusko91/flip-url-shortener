package io.geflip.urlshortener.domain.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

public record OriginalUrl(String value) {
  @JsonCreator
  public OriginalUrl {
    Objects.requireNonNull(value, "URL cannot be null");
    if (value.isBlank()) {
      throw new IllegalArgumentException("URL cannot be blank");
    }
    // TODO validate URL
  }

  public static OriginalUrl of(final String value) {
    return new OriginalUrl(value);
  }

  @JsonValue
  @Override
  public String toString() {
    return value;
  }
}
