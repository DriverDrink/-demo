package com.dhu.domain.returnDomain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class MyGrade {

    @TableId
    private String c_name;

    private Integer score;

    private Integer credit;

    private String term;
}
