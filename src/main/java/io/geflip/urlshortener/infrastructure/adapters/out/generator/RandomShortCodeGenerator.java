package io.geflip.urlshortener.infrastructure.adapters.out.generator;

import io.geflip.urlshortener.domain.model.value.ShortCode;
import io.geflip.urlshortener.domain.ports.out.generator.ShortCodeGeneratorPort;
import io.geflip.urlshortener.infrastructure.configuration.UrlShortenerProperties;
import io.geflip.urlshortener.infrastructure.util.Base62RandomGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class RandomShortCodeGenerator implements ShortCodeGeneratorPort {

  private final UrlShortenerProperties urlShortenerProperties;

  @Override
  public ShortCode generateShortCode() {
    return ShortCode.of(
        Base62RandomGenerator.generateBase62(urlShortenerProperties.getShortCodeLength()));
  }
}
