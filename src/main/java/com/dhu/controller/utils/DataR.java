package com.dhu.controller.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//数据封装
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataR {
    Boolean flag;
    Object data;
    String msg;

    public DataR(Boolean flag) {
        this.flag = flag;
    }

    public DataR(Boolean flag,Object data) {
        this.flag = flag;
        this.data = data;
    }

    public DataR(String msg){
        this.flag = false;
        this.msg = msg;
    }

}
