package com.wsiz.gameshub.exceptionhandler;

import com.wsiz.gameshub.exception.ObjectNotFoundException;
import com.wsiz.gameshub.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value
            = {ObjectNotFoundException.class})
    protected ResponseEntity<ObjectNotFoundException> handleNotFoundException(
            ObjectNotFoundException ex) {
        log.error("Object not found {}, {}", ex.getObjectId(), ex.getMessage());

        return ResponseEntity.badRequest().body(ex);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Object> handleMethodArgumentNotValid (MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult result = ex.getBindingResult();

        List<FieldError> fieldErrors = result.getFieldErrors();

        String errorMessage = fieldErrors.get(0).getDefaultMessage();

        return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
    }
}
