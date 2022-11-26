package com.rioa.configurer.respond;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler {
    @Autowired
    private BasicErrorController basicErrorController;

    @ExceptionHandler(Throwable.class)
    public ResponseData handleException(Throwable t, HttpServletResponse response) throws Throwable {
        return ResponseData.Companion.error(ResponseCode.FAILURE.getCode(), t.getMessage(), null);
    }

    @GetMapping("${server.error.path:${error.path:/error}}")
    public ResponseData error(Throwable t, HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity<Map<String, Object>> error = basicErrorController.error(request);
        Map<String, Object> body;
        String notExistingPath = error == null ? null : ((body = error.getBody()) == null ? null : String.valueOf(body.get("path")));
        return ResponseData.Companion.error(error.getStatusCodeValue(), error.getStatusCode().name(), null);
    }
}