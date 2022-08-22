package com.dhu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dhu.dao.sqlSentence.Sql;
import com.dhu.domain.Teacher;
import com.dhu.domain.returnDomain.CourseGrade;
import com.dhu.domain.returnDomain.CourseGradeInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface TeacherDao extends BaseMapper<Teacher> {

    @SelectProvider(type = Sql.class,method = "selectCGrade")
    List<CourseGrade> selectCGrade(@Param("tid") Integer tid, @Param("cid")Integer cid, @Param("sid")Integer sid);

    @SelectProvider(type = Sql.class,method = "gradeStatisticsT")
    CourseGradeInfo gradeStatisticsT(@Param("c_id") Integer c_id);
}
