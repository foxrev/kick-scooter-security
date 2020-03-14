package com.softServe.security.exceptionHandler;

import com.softServe.security.exception.AuthorizationException;
import com.softServe.security.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.ServletException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class AuthExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> authExceptionHandler(Exception e, WebRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                .time(LocalDateTime.now())
                                                .status(HttpStatus.UNAUTHORIZED.value())
                                                .error(e.getMessage())
                                                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ErrorResponse> servletException(Exception e, WebRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                    .time(LocalDateTime.now())
                                                    .status(500)
                                                    .error(e.getMessage())
                                                    .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
