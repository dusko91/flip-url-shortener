package io.geflip.urlshortener.infrastructure.adapters.out.generator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.geflip.urlshortener.domain.model.value.ShortCode;
import io.geflip.urlshortener.infrastructure.configuration.UrlShortenerProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("RandomShortCodeGenerator")
class RandomShortCodeGeneratorTest {

  @Mock private UrlShortenerProperties properties;

  @InjectMocks private RandomShortCodeGenerator shortCodeGenerator;

  @Test
  @DisplayName("Should generate short code using configured length")
  void testGenerateShortCode() {
    // given
    int expectedLength = 10;
    when(properties.getShortCodeLength()).thenReturn(expectedLength);

    // when
    ShortCode result = shortCodeGenerator.generateShortCode();

    // then
    assertThat(result.value()).hasSize(expectedLength);
    verify(properties).getShortCodeLength();
  }
}
