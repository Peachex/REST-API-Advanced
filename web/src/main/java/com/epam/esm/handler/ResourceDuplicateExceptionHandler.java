package com.epam.esm.handler;

import com.epam.esm.dao.constant.Symbol;
import com.epam.esm.exception.ExceptionResponse;
import com.epam.esm.exception.ResourceDuplicateException;
import com.epam.esm.util.ExceptionMessageLocale;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceDuplicateExceptionHandler {
    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    @ExceptionHandler(ResourceDuplicateException.class)
    public final ResponseEntity<ExceptionResponse> handleRuntimeExceptions(ResourceDuplicateException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getLocalizedMessage(
                ExceptionMessageLocale.getCurrent()) + Symbol.SPACE_SYMBOL + e.getDetail(), e.getErrorCode());
        exceptionResponse.setErrorCode(status.value() + e.getErrorCode());
        return new ResponseEntity<>(exceptionResponse, status);
    }
}
