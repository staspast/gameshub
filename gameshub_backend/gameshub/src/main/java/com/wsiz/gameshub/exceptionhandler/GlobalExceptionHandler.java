package com.wsiz.gameshub.exceptionhandler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.wsiz.gameshub.exception.ObjectNotFoundException;
import com.wsiz.gameshub.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { ObjectNotFoundException.class })
    protected ResponseEntity<ObjectNotFoundException> handleNotFoundException(
            ObjectNotFoundException ex) {
        log.error("Object not found {}, {}", ex.getObjectId(), ex.getMessage());

        return ResponseEntity.badRequest().body(ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();

        List<FieldError> fieldErrors = result.getFieldErrors();

        String errorMessage = fieldErrors.get(0).getDefaultMessage();

        return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
    }
}
