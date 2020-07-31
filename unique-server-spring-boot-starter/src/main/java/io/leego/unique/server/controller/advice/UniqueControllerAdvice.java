package io.leego.unique.server.controller.advice;

import io.leego.unique.common.Result;
import io.leego.unique.common.util.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Yihleego
 */
@RestControllerAdvice(basePackages = "io.leego.unique.server")
public class UniqueControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(UniqueControllerAdvice.class);

    @ExceptionHandler
    public Result<Void> catchNotValidException(MethodArgumentNotValidException e) {
        FieldError error = e.getBindingResult().getFieldError();
        return Result.buildFailure(ErrorCode.ERROR, error != null ? error.getDefaultMessage() : e.getMessage());
    }

    @ExceptionHandler
    public Result<Void> catchAllException(Exception e) {
        logger.error("", e);
        return Result.buildFailure(ErrorCode.ERROR, e.getMessage());
    }

}
