package com.dhu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dhu.domain.Student;
import com.dhu.domain.returnDomain.MyGrade;

public interface IStudentService extends IService<Student> {

    String login(Integer username,String password);

    boolean alterPassword(Integer username,String newPassword);

    IPage<MyGrade> getSelfGrade(Integer id, String term, int currentPage, int pageSize);
}
