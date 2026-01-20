package io.geflip.urlshortener.domain.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public record ShortCode(String value) {
  @JsonCreator
  public ShortCode {
    if (value == null || value.length() >= 20 || value.isBlank()) {
      throw new IllegalArgumentException("Short code has to be less than 20 characters long!");
    }
  }

  public static ShortCode of(final String value) {
    return new ShortCode(value);
  }

  @JsonValue
  @Override
  public String toString() {
    return value;
  }
}
