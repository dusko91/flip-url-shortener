package io.geflip.urlshortener.infrastructure.adapters.in.controllers.exception;

record HttpErrorResponse(String statusCode, String subStatusCode, String message) {}
