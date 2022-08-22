package com.dhu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dhu.domain.Course;
import com.dhu.domain.Manager;
import com.dhu.domain.Student;
import com.dhu.domain.Teacher;
import com.dhu.domain.returnDomain.GradeInfo;



public interface IManagerService extends IService<Manager> {

    String login(Integer username,String password);

    boolean alterPassword(Integer username,String newPassword);

    IPage<Student> Stu(int currentPage,int pageSize,Student student);

    Student getOneStu(Integer sid);

    boolean saveStu(Student student);

    boolean updateStu(Student student);

    boolean deleteStu(Student student);

    IPage<Teacher> Tea(int currentPage, int pageSize,Teacher teacher);

    Teacher getOneTea(Integer tid);

    boolean saveTea(Teacher teacher);

    boolean updateTea(Teacher teacher);

    boolean deleteTea(Teacher teacher);

    IPage<Course> COU(int currentPage, int pageSize, Course course);

    Course getOneCou(Integer cid);

    boolean saveCou(Course course);

    boolean updateCou(Course course);

    boolean deleteCou(Course course);

    IPage<GradeInfo> Statistics(String CName,int currentPage,int pageSize,String condition);

}
