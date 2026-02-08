package com.sv.meeting.api.exception;



import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ApiError> conflict(ConflictException ex, HttpServletRequest req) {
    return ResponseEntity.status(409).body(ApiError.of(409, "CONFLICT", ex.getMessage(), req.getRequestURI()));
  }
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiError> notFound(NotFoundException ex, HttpServletRequest req) {
    return ResponseEntity.status(404).body(ApiError.of(404, "NOT_FOUND", ex.getMessage(), req.getRequestURI()));
  }
  @ExceptionHandler({ BadRequestException.class, MethodArgumentNotValidException.class })
  public ResponseEntity<ApiError> badRequest(Exception ex, HttpServletRequest req) {
    return ResponseEntity.status(400).body(ApiError.of(400, "BAD_REQUEST", ex.getMessage(), req.getRequestURI()));
  }
}
