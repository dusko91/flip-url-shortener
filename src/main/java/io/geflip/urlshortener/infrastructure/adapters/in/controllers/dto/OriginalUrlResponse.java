package io.geflip.urlshortener.infrastructure.adapters.in.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "OriginalUrlResponse",
    description = "Response returned when retrieving the original URL for a short URL")
public record OriginalUrlResponse(
    @Schema(
            description = "The original URL corresponding to the short URL",
            example = "https://example.com",
            required = true)
        String originalUrl) {}
