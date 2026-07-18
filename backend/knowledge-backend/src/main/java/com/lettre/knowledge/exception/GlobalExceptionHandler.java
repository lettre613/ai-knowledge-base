package com.lettre.knowledge.exception;


import com.lettre.knowledge.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(
            BusinessException e
    ){

        return Result.error(
                e.getCode(),
                e.getMessage()
        );

    }


    @ExceptionHandler(Exception.class)
    public Result<?> handleException(
            Exception e
    ){

        return Result.error(
                50000,
                "服务器异常"
        );

    }

}