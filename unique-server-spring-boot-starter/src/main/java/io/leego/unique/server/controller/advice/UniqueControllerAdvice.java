package io.leego.unique.server.controller.advice;

import io.leego.unique.common.Result;
import io.leego.unique.common.util.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Yihleego
 */
@RestControllerAdvice(basePackages = "io.leego.unique.server")
public class UniqueControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(UniqueControllerAdvice.class);

    /**
     * Catches exceptions
     * @param e Exception
     * @return {@link Result}
     */
    @ExceptionHandler
    public Result<Void> catchAllException(Exception e) {
        logger.error("", e);
        return Result.buildFailure(ErrorCode.ERROR, e.getMessage());
    }

}
