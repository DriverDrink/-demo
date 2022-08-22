package com.dhu.domain.returnDomain;

import lombok.Data;

@Data
public class GradeInfo {

    private String s_department;

    private  String s_major;

    private  String s_class;

    private  Integer failCount;

    private Integer lowestScore;

    private  Integer highestScore;

    private  Integer averageScore;
}
