package io.geflip.urlshortener.infrastructure.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "url-shortener")
public class UrlShortenerProperties {
  /** Length of the randomly generated short code. */
  private int shortCodeLength = 10;

  /** Base URL used to build the short URLs. */
  private String baseUrl;
}
