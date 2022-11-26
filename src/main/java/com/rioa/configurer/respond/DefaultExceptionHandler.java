package com.rioa.configurer.respond;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ AuthenticationException.class })
    @ResponseBody
    public ResponseEntity<ResponseData> handleAuthenticationException(Exception ex) {

        ResponseData re = new ResponseData(ResponseCode.UNAUTHORIZED.getCode(),
          ResponseCode.UNAUTHORIZED.getMessage(), null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(re);
    }
}