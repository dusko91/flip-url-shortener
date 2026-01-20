package io.geflip.urlshortener.infrastructure.adapters.in.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "ShortUrlRequest", description = "Request body for shortening a URL")
public record ShortUrlRequest(
    @NotNull
        @NotBlank
        @Schema(
            description = "The original URL to shorten",
            example = "https://example.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
        String originalUrl) {}
