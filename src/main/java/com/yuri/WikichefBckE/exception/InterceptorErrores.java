package com.yuri.WikichefBckE.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
public class InterceptorErrores extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<LogError> manejarTodasExceptciones(Exception ex, WebRequest request){
        LogError er = new LogError(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(er, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoEncontradoException.class)
    public ResponseEntity<LogError> manejarNoEncontradoException(NoEncontradoException ex, WebRequest request){
        LogError er = new LogError(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String mensaje = ex.getBindingResult().getAllErrors().stream().map(e -> {
            return e.getDefaultMessage().concat(",");
        }).collect(Collectors.joining());
        LogError er = new LogError(LocalDateTime.now(), mensaje, request.getDescription(false));
        return new ResponseEntity<>(er, HttpStatus.BAD_REQUEST);
    }
}