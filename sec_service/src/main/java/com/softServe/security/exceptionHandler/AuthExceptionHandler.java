package com.softServe.security.exceptionHandler;

import com.softServe.security.exception.AuthorizationException;
import com.softServe.security.model.AuthenticationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.ServletException;
import java.time.LocalDateTime;

@ControllerAdvice
public class AuthExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<AuthenticationErrorResponse> authExceptionHandler(Exception e, WebRequest request){
        AuthenticationErrorResponse errorResponse = AuthenticationErrorResponse.builder()
                                                .time(LocalDateTime.now())
                                                .status(HttpStatus.NOT_ACCEPTABLE.value())
                                                .error(e.getMessage())
                                                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<AuthenticationErrorResponse> servletException(Exception e, WebRequest request){
        AuthenticationErrorResponse errorResponse = AuthenticationErrorResponse.builder()
                                                    .time(LocalDateTime.now())
                                                    .status(500)
                                                    .error(e.getMessage())
                                                    .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
