package io.geflip.urlshortener.infrastructure.adapters.in.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.geflip.urlshortener.domain.model.value.OriginalUrl;
import io.geflip.urlshortener.domain.model.value.ShortUrl;
import io.geflip.urlshortener.domain.ports.in.url.UrlExpanderPort;
import io.geflip.urlshortener.domain.ports.in.url.UrlShortenerPort;
import io.geflip.urlshortener.infrastructure.adapters.in.controllers.dto.ShortUrlRequest;
import io.geflip.urlshortener.infrastructure.adapters.in.controllers.exception.ErrorResponses;
import io.geflip.urlshortener.infrastructure.adapters.in.controllers.exception.GlobalControllerExceptionHandler;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = {UrlShortenerRestController.class})
@Import(GlobalControllerExceptionHandler.class)
public class UrlShortenerRestControllerIT {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private UrlShortenerPort urlShortenerPort;

  @MockitoBean private UrlExpanderPort urlExpanderPort;

  @Test
  @DisplayName("Should shorten a URL and return 201 with shortUrl in body")
  void testShortenUrl() throws Exception {
    ShortUrlRequest request = new ShortUrlRequest("https://example.com");
    ShortUrl shortUrl = ShortUrl.of("https://example.com/abc123");

    Mockito.when(urlShortenerPort.shortenUrl(any(OriginalUrl.class))).thenReturn(shortUrl);

    mockMvc
        .perform(
            post("/api/v1/urls/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.shortUrl").value(shortUrl.value()));
  }

  @Test
  @DisplayName("Should return original URL when short URL exists")
  void testGetOriginalUrlFound() throws Exception {
    OriginalUrl originalUrl = OriginalUrl.of("https://example.com");

    Mockito.when(urlExpanderPort.getOriginalUrl(eq(ShortUrl.of("https://sh.url.io/abc123"))))
        .thenReturn(Optional.of(originalUrl));

    mockMvc
        .perform(get("/api/v1/urls/expand").param("shortUrl", "https://sh.url.io/abc123"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.originalUrl").value(originalUrl.value()));
  }

  @Test
  @DisplayName("Should return 404 when short URL is not found")
  void testGetOriginalUrlNotFound() throws Exception {
    Mockito.when(urlExpanderPort.getOriginalUrl(eq(ShortUrl.of("https://sh.url.io/abc123"))))
        .thenReturn(Optional.empty());

    mockMvc
        .perform(get("/api/v1/urls/expand").param("shortUrl", "https://sh.url.io/abc123"))
        .andExpect(status().isNotFound())
        .andExpect(
            jsonPath("$.statusCode").value(ErrorResponses.RESOURCE_NOT_FOUND.getStatusCode()))
        .andExpect(
            jsonPath("$.subStatusCode").value(ErrorResponses.RESOURCE_NOT_FOUND.getSubStatusCode()))
        .andExpect(jsonPath("$.message").value(ErrorResponses.RESOURCE_NOT_FOUND.getMessage()));
  }

  @Test
  @DisplayName("Should return 400 when IllegalArgumentException is thrown during expansion")
  void testGetOriginalUrlWithIllegalArgument() throws Exception {
    Mockito.when(urlExpanderPort.getOriginalUrl(any(ShortUrl.class)))
        .thenThrow(new IllegalArgumentException("Invalid short URL format"));

    mockMvc
        .perform(get("/api/v1/urls/expand").param("shortUrl", "invalid"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.statusCode").value(ErrorResponses.BAD_REQUEST.getStatusCode()))
        .andExpect(jsonPath("$.subStatusCode").value(ErrorResponses.BAD_REQUEST.getSubStatusCode()))
        .andExpect(jsonPath("$.message").value(ErrorResponses.BAD_REQUEST.getMessage()));
  }

  @Test
  @DisplayName("Should return 500 when unexpected exception is thrown during expansion")
  void testGetOriginalUrlWithUnexpectedException() throws Exception {
    Mockito.when(urlExpanderPort.getOriginalUrl(any(ShortUrl.class)))
        .thenThrow(new RuntimeException("Cache lookup failed"));

    mockMvc
        .perform(get("/api/v1/urls/expand").param("shortUrl", "https://sh.url.io/abc123"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.statusCode").value(ErrorResponses.UNEXPECTED_ERROR.getStatusCode()))
        .andExpect(
            jsonPath("$.subStatusCode").value(ErrorResponses.UNEXPECTED_ERROR.getSubStatusCode()))
        .andExpect(jsonPath("$.message").value(ErrorResponses.UNEXPECTED_ERROR.getMessage()));
  }
}
