package io.geflip.urlshortener.infrastructure.adapters.in.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "ShortUrlRequest", description = "Request body for shortening a URL")
public record ShortUrlRequest(
    @NotNull
        @NotBlank
        @Size(min = 1, max = 2048)
        @Schema(
            description = "The original URL to shorten",
            example = "https://example.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
        String originalUrl) {}
