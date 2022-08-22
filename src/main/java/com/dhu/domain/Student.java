package com.dhu.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Student {

    @TableId("s_id")
    private Integer s_id;

    private String s_name;

    private String s_password;

    private String s_sex;

    private String s_department;

    private String s_major;

    private String s_class;

}
