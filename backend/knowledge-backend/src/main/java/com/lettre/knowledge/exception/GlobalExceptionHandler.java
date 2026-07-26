package com.lettre.knowledge.exception;


import com.lettre.knowledge.common.Result;

import org.springframework.web.bind.MethodArgumentNotValidException;

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



    /**
     * 处理 @Valid 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidationException(
            MethodArgumentNotValidException e
    ){


        String message =
                e.getBindingResult()
                        .getFieldErrors()
                        .get(0)
                        .getDefaultMessage();


        return Result.error(
                40000,
                message
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