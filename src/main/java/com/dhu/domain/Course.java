package com.dhu.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Course {
    @TableId("c_id")
    private Integer c_id;
    private String c_name;
    private String term;
    private Integer credit;
    private Integer t_id;
}
