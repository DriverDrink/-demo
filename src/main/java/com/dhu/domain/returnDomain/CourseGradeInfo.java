package com.dhu.domain.returnDomain;

import lombok.Data;

@Data
public class CourseGradeInfo {

    private Integer failCount;

    private Integer B60_69Count;

    private Integer B70_79Count;

    private Integer B80_89Count;

    private Integer B90_100Count;

    private Integer lowestScore;

    private Integer highestScore;

    private Integer averageScore;

}
