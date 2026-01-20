package io.geflip.urlshortener.infrastructure.adapters.in.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ShortUrlResponse", description = "Response returned after shortening a URL")
public record ShortUrlResponse(
    @Schema(
            description = "The generated short URL",
            example = "https://url-shortener.getflip.com/abc123",
            required = true)
        String shortUrl) {}
