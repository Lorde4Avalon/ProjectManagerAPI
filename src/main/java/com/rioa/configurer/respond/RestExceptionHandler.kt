package com.rioa.configurer.respond

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class RestExceptionHandler {
    @ExceptionHandler(Exception::class)
    @ResponseStatus(value = org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(exception: Throwable): ResponseData<String> {
        return ResponseData.error(code = ResponseCode.INTERNAL_SERVER_ERROR.code,exception.message ?: ResponseCode.INTERNAL_SERVER_ERROR.message)
    }
}