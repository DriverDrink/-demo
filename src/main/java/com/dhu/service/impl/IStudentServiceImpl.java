package com.dhu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhu.dao.StudentDao;
import com.dhu.domain.Student;
import com.dhu.domain.returnDomain.MyGrade;
import com.dhu.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class IStudentServiceImpl extends ServiceImpl<StudentDao,Student> implements IStudentService{

    @Autowired
    private StudentDao studentDao;

    @Override
    public String login(Integer username, String password) {
        LambdaQueryWrapper<Student> lqw = new LambdaQueryWrapper<Student>().eq(Student::getS_id,username);
        Student student = studentDao.selectOne(lqw);
        if(ObjectUtils.isEmpty(student))
            return "学号不存在";
        lqw.eq(Student::getS_password,password);
        student = studentDao.selectOne(lqw);
        if(ObjectUtils.isEmpty(student))
            return "密码不正确，请重试";
        else
            return student.getS_name();
    }

    @Override
    public boolean alterPassword(Integer username,String newPassword) {
        Student student=new Student();
        LambdaUpdateWrapper<Student> luw= new LambdaUpdateWrapper<Student>().eq(Student::getS_id,username).set(Student::getS_password,newPassword);
        int i = studentDao.update(student,luw);
        return i == 1;
    }

    @Override
    public IPage<MyGrade> getSelfGrade(Integer id, String term, int currentPage, int pageSize) {
        List<MyGrade> myGradeList = studentDao.getAllGrade(id,term);
        List<MyGrade> pageList = new ArrayList<>();
        IPage<MyGrade> page = new Page<>(currentPage,pageSize);
        int curIdx = currentPage > 1 ? (currentPage - 1) * pageSize : 0;
        for (int i = 0; i < pageSize && curIdx + i < myGradeList.size(); i++) {
            pageList.add(myGradeList.get(curIdx + i));
        }
        page.setRecords(pageList);
        page.setTotal(myGradeList.size());
        return page;
    }

}
