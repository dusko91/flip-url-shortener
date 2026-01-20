package io.geflip.urlshortener.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.geflip.urlshortener.infrastructure.adapters.in.controllers.dto.ShortUrlRequest;
import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Url Shorten and Expand Workflow")
class UrlShortenExpandWorkflowIT {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @DisplayName("Shorten a URL and then Expand it")
  void shortenExpandWorkflow() throws Exception {
    String originalUrl = "https://original.long.url.com/q?queryParam=123";
    ShortUrlRequest shortenRequest = new ShortUrlRequest(originalUrl);

    // 1. POST to shorten the URL
    String postResponse =
        mockMvc
            .perform(
                post(URI.create("/api/v1/urls/shorten"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(shortenRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.shortUrl").exists())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Extract the generated short URL from response
    String generatedShortUrl = JsonPath.read(postResponse, "$.shortUrl");

    // 2. GET to expand the URL using the result from Step 1
    mockMvc
        .perform(get("/api/v1/urls/expand").param("shortUrl", generatedShortUrl))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.originalUrl").value(originalUrl));
  }
}
