package com.dhu.domain.returnDomain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class CourseGrade {

    @TableId
    private Integer s_id;

    private String s_class;

    private String s_name;

    private Integer score;

    private String c_name;

    private Integer c_id;

}
