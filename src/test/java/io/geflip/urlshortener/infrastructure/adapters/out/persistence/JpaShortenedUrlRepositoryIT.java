package io.geflip.urlshortener.infrastructure.adapters.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import io.geflip.urlshortener.infrastructure.adapters.out.persistence.entity.ShortenedUrlEntity;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("JpaShortenedUrlRepository")
class JpaShortenedUrlRepositoryIT {

  @Autowired private JpaShortenedUrlRepository repository;

  @Test
  @DisplayName("Should save entity and find it by short code")
  void testSaveAndFindByShortCode() {
    // given
    ShortenedUrlEntity entity = new ShortenedUrlEntity(null, "https://example.com", "ex123");
    repository.save(entity);

    // when
    Optional<ShortenedUrlEntity> found = repository.findByShortCode("ex123");

    // then
    assertThat(found).isPresent();
    assertThat(found.get().getOriginalUrl()).isEqualTo("https://example.com");
  }

  @Test
  @DisplayName("Should verify existence by short code")
  void testExistsByShortCode() {
    // given
    ShortenedUrlEntity entity = new ShortenedUrlEntity(null, "https://example.com", "ex123");
    repository.save(entity);

    // when
    boolean exists = repository.existsByShortCode("ex123");
    boolean notExists = repository.existsByShortCode("unknown");

    // then
    assertThat(exists).isTrue();
    assertThat(notExists).isFalse();
  }

  @Test
  @DisplayName("Should find by original URL and return matching entity")
  void testFindByOriginalUrl() {
    // given
    ShortenedUrlEntity entity = new ShortenedUrlEntity(null, "https://example.com", "ex123");
    repository.save(entity);

    // when
    Optional<ShortenedUrlEntity> found = repository.findByOriginalUrl("https://example.com");

    // then
    assertThat(found).isPresent();
    assertThat(found.get().getShortCode()).isEqualTo("ex123");
  }
}
