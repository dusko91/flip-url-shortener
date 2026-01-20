package io.geflip.urlshortener.infrastructure.adapters.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shortened_urls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShortenedUrlEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String originalUrl;

  @Column(nullable = false)
  private String shortCode;
}
