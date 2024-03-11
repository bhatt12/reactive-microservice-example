package com.example.web.handler;

import io.netty.handler.codec.rtsp.RtspResponseEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.ControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleResponse(WebExchangeBindException ex){

        log.error("Exception {}", ex.getMessage(), ex);
        var error = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .sorted()
                .collect(Collectors.joining(","));
        log.error(error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }
}
