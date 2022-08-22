package com.dhu.controller.utils;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@Order(1)
@RestControllerAdvice
public class SqlExceptionAdvice {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public DataR sqlException(SQLIntegrityConstraintViolationException ex){
        ex.printStackTrace();
        return new DataR("请检查数据的正确性！");
    }

}
