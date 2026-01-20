package io.geflip.urlshortener.infrastructure.util;

import java.security.SecureRandom;

public class Base62RandomGenerator {
  private static final String BASE62_CHARS =
      "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  private static final SecureRandom random = new SecureRandom();

  /** Generates a random string of the specified length containing only base62 characters. */
  public static String generateBase62(int length) {
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int randomIndex = random.nextInt(BASE62_CHARS.length());
      sb.append(BASE62_CHARS.charAt(randomIndex));
    }
    return sb.toString();
  }
}
