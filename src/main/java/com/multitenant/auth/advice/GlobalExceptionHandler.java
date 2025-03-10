package com.multitenant.auth.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.multitenant.auth.exception.ResourceNotFoundException;

import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourcNotFoundException(ResourceNotFoundException exception){
        ApiError apiError = new ApiError(exception.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex){
        ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException ex){
        ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex){
        ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex){
        ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.CONFLICT);
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException ex){
        ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException ex){
        ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        ApiError apiError = new ApiError(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
