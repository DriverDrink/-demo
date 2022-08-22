package com.dhu.controller.utils;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Order(100)
@RestControllerAdvice
public class ProjectExceptionAdvice {

    @ExceptionHandler()
    public DataR doException(Exception ex){
        ex.printStackTrace();
        return new DataR("服务器故障请重试！");
    }

}
