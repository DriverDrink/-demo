package com.dhu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dhu.domain.Course;
import com.dhu.domain.Grade;
import com.dhu.domain.Teacher;
import com.dhu.domain.returnDomain.CourseGrade;
import com.dhu.domain.returnDomain.CourseGradeInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public interface ITeacherService extends IService<Teacher> {

    String login(Integer username,String password);

    boolean alterPassword(Integer username,String newPassword);

    List<Course> getMyCourse(Integer tid);

    IPage<CourseGrade> selectCourseGrade(Integer tid, Grade grade, int currentPage, int pageSize);

    CourseGrade getOne(Integer sid, Integer cid);

    String insert(CourseGrade courseGrade, Integer tid);

    boolean delete(CourseGrade courseGrade);

    boolean update(CourseGrade courseGrade);

    CourseGradeInfo StatisticsT(Integer cid, Integer tid);

    String[] excelToMysql (InputStream inputStream, Integer tid) throws IOException;

}
