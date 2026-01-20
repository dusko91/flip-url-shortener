package io.geflip.urlshortener.infrastructure.adapters.in.controllers;

import io.geflip.urlshortener.domain.model.value.OriginalUrl;
import io.geflip.urlshortener.domain.model.value.ShortUrl;
import io.geflip.urlshortener.domain.ports.in.url.UrlExpanderPort;
import io.geflip.urlshortener.domain.ports.in.url.UrlShortenerPort;
import io.geflip.urlshortener.infrastructure.adapters.in.controllers.dto.OriginalUrlResponse;
import io.geflip.urlshortener.infrastructure.adapters.in.controllers.dto.ShortUrlRequest;
import io.geflip.urlshortener.infrastructure.adapters.in.controllers.dto.ShortUrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/urls")
public class UrlShortenerRestController {

  private final UrlExpanderPort urlExpanderPort;
  private final UrlShortenerPort urlShortenerPort;

  @Operation(summary = "Shorten a given URL")
  @PostMapping("/shorten")
  public ResponseEntity<ShortUrlResponse> shortenUrl(@RequestBody @Valid ShortUrlRequest request) {

    final OriginalUrl originalUrl = OriginalUrl.of(request.originalUrl());
    ShortUrl shortUrl = urlShortenerPort.shortenUrl(originalUrl);
    return ResponseEntity.status(HttpStatus.CREATED).body(new ShortUrlResponse(shortUrl.value()));
  }

  @GetMapping("/expand")
  @Operation(summary = "Get the original URL for a given short URL")
  public ResponseEntity<OriginalUrlResponse> getOriginalUrl(
      @Parameter(description = "Short URL", required = true) @RequestParam String shortUrl) {
    Optional<OriginalUrl> originalUrl = urlExpanderPort.getOriginalUrl(ShortUrl.of(shortUrl));
    return originalUrl
        .map(url -> ResponseEntity.ok(new OriginalUrlResponse(url.value())))
        .orElseThrow(() -> new NoSuchElementException("The shortUrl:" + shortUrl + " is missing!"));
  }
}
