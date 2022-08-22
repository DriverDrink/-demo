package com.dhu.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Teacher {

    @TableId("t_id")
    private Integer t_id;

    private String t_name;

    private String t_password;

    private String t_sex;

    private String telephone;

    private String t_department;

}
