package io.geflip.urlshortener.infrastructure.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Base62RandomGenerator")
class Base62RandomGeneratorTest {

  private static final String BASE62_CHARS =
      "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

  @Test
  @DisplayName("Should return Base62 string of requested length")
  void generateBase62_shouldReturnStringOfCorrectLength() {
    int length = 10;
    String result = Base62RandomGenerator.generateBase62(length);
    assertThat(result).hasSize(length);
  }

  @Test
  @DisplayName("Should contain only valid Base62 characters")
  void generateBase62_shouldContainOnlyValidBase62Characters() {
    int length = 50;
    String result = Base62RandomGenerator.generateBase62(length);

    for (final char c : result.toCharArray()) {
      assertThat(BASE62_CHARS).contains(String.valueOf(c));
    }
  }

  @Test
  @DisplayName("Should produce different values across multiple generations")
  void generateBase62_shouldProduceDifferentResults() {
    int length = 20;
    Set<String> results = new HashSet<>();
    for (int i = 0; i < 100; i++) {
      results.add(Base62RandomGenerator.generateBase62(length));
    }
    // There should be more than 90 unique values (to avoid extremely unlikely collisions)
    assertThat(results.size()).isGreaterThan(90);
  }
}
