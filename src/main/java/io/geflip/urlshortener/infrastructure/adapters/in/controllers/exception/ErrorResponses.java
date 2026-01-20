package io.geflip.urlshortener.infrastructure.adapters.in.controllers.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorResponses {
  UNEXPECTED_ERROR(
      new HttpErrorResponse(
          String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
          "50001",
          "Unexpected error occurred.")),
  RESOURCE_NOT_FOUND(
      new HttpErrorResponse(
          String.valueOf(HttpStatus.NOT_FOUND.value()), "40001", "Resource not found.")),
  BAD_REQUEST(
      new HttpErrorResponse(
          String.valueOf(HttpStatus.BAD_REQUEST.value()),
          "50001",
          "Bad request. Please check your request parameters and try again.")) {};

  public String getMessage() {
    return this.httpErrorResponse.message();
  }

  public String getStatusCode() {
    return this.httpErrorResponse.statusCode(); // Assuming this field name
  }

  public String getSubStatusCode() {
    return this.httpErrorResponse.subStatusCode(); // Assuming this field name
  }

  private final HttpErrorResponse httpErrorResponse;
}
