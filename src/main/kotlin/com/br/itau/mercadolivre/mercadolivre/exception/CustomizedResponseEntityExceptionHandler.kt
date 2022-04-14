package com.br.itau.mercadolivre.mercadolivre.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException


@ControllerAdvice
@RestController
class CustomizedResponseEntityExceptionHandler:ResponseEntityExceptionHandler() {
    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(exception:Exception,webRequest:WebRequest):ResponseEntity<ExceptionResponse>{
val exceptionResponse = ExceptionResponse(Date(),exception.message,webRequest.getDescription(false))
        return ResponseEntity(exceptionResponse,HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(constraintViolationException:ConstraintViolationException,webRequest:WebRequest):ResponseEntity<ExceptionResponse>{
        val message = StringBuilder()
        val violations: Set<ConstraintViolation<*>> = constraintViolationException.getConstraintViolations()
        for (violation in violations) {
            message.append(violation.message + ";")
        }
        val exceptionResponse = ExceptionResponse(Date(),message.toString(),webRequest.getDescription(false))
        return ResponseEntity.badRequest().body(exceptionResponse)
    }
}