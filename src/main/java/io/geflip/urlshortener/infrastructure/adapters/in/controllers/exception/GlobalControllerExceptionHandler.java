package io.geflip.urlshortener.infrastructure.adapters.in.controllers.exception;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
  @ExceptionHandler(Exception.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<HttpErrorResponse> handleUnexpectedException(Exception ex) {
    HttpErrorResponse error = ErrorResponses.UNEXPECTED_ERROR.getHttpErrorResponse();
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<HttpErrorResponse> handleBadRequest(IllegalArgumentException ex) {
    HttpErrorResponse error = ErrorResponses.BAD_REQUEST.getHttpErrorResponse();
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<HttpErrorResponse> handleRessourceNotFound(NoSuchElementException ex) {
    HttpErrorResponse error = ErrorResponses.RESOURCE_NOT_FOUND.getHttpErrorResponse();
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }
}
