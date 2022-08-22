package com.dhu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhu.dao.*;
import com.dhu.domain.*;
import com.dhu.domain.returnDomain.GradeInfo;
import com.dhu.service.IManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class IManagerServiceImpl extends ServiceImpl<ManagerDao,Manager> implements IManagerService{
    @Autowired
    private ManagerDao managerDao;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private GradeDao gradeDao;

    @Override
    public String login(Integer username, String password) {
        LambdaQueryWrapper<Manager> lqw = new LambdaQueryWrapper<Manager>().eq(Manager::getM_id,username);
        Manager manager = managerDao.selectOne(lqw);
        if(ObjectUtils.isEmpty(manager))
            return "管理员账户不存在";
        lqw.eq(Manager::getM_password,password);
        manager = managerDao.selectOne(lqw);
        if(ObjectUtils.isEmpty(manager))
            return "密码不正确，请重试";
        else
            return manager.getM_name();
    }

    @Override
    public boolean alterPassword(Integer username, String newPassword) {
        Manager manager=new Manager();
        LambdaUpdateWrapper<Manager> luw= new LambdaUpdateWrapper<Manager>().eq(Manager::getM_id,username).set(Manager::getM_password,newPassword);
        int i = managerDao.update(manager,luw);
        return i == 1;
    }

    @Override
    public IPage<Student> Stu(int currentPage,int pageSize,Student student){
        LambdaQueryWrapper<Student> lqw = new LambdaQueryWrapper<Student>().like(student.getS_id() !=null,Student::getS_id,student.getS_id());
        IPage<Student> page= new Page<>(currentPage, pageSize);
        studentDao.selectPage(page,lqw);
        return page;
    }

    @Override
    public Student getOneStu(Integer sid) {
        return studentDao.selectById(sid);
    }

    @Override
    public boolean saveStu(Student student) {
        return studentDao.insert(student) > 0;
    }

    @Override
    public boolean updateStu(Student student) {
        return studentDao.updateById(student) > 0;
    }

    @Override
    public boolean deleteStu(Student student) {
        LambdaUpdateWrapper<Grade> lqw = new LambdaUpdateWrapper<Grade>().eq(Grade::getS_id,student.getS_id());
        gradeDao.delete(lqw);
        return studentDao.deleteById(student) > 0;
    }

    @Override
    public IPage<Teacher> Tea(int currentPage, int pageSize,Teacher teacher) {
        LambdaQueryWrapper<Teacher> lqw = new LambdaQueryWrapper<Teacher>().like(teacher.getT_id() !=null,Teacher::getT_id,teacher.getT_id());
        IPage<Teacher> page= new Page<>(currentPage,pageSize);
        teacherDao.selectPage(page,lqw);
        return page;
    }

    @Override
    public Teacher getOneTea(Integer tid) {
        return teacherDao.selectById(tid);
    }

    @Override
    public boolean saveTea(Teacher teacher) {
        return teacherDao.insert(teacher) > 0;
    }

    @Override
    public boolean updateTea(Teacher teacher) {
        return teacherDao.updateById(teacher) > 0;
    }

    @Override
    public boolean deleteTea(Teacher teacher) {
        LambdaUpdateWrapper<Course> lqw = new LambdaUpdateWrapper<Course>().eq(Course::getT_id,teacher.getT_id());
        List<Course> courseList = courseDao.selectList(lqw);
        for (Course course : courseList) {
            deleteCou(course);
        }
        courseDao.delete(lqw);
        return teacherDao.deleteById(teacher) > 0;
    }

    @Override
    public IPage<Course> COU(int currentPage, int pageSize,Course course) {
        LambdaUpdateWrapper<Course> lqw = new LambdaUpdateWrapper<Course>().like(course.getC_id() !=null,Course::getC_id,course.getC_id());
        IPage<Course> page= new Page<>(currentPage,pageSize);
        courseDao.selectPage(page,lqw);
        return page;
    }

    @Override
    public Course getOneCou(Integer cid) {
        return courseDao.selectById(cid);
    }

    @Override
    public boolean saveCou(Course course) {
        return courseDao.insert(course) > 0;
    }

    @Override
    public boolean updateCou(Course course) {
        return courseDao.updateById(course) > 0;
    }

    @Override
    public boolean deleteCou(Course course) {
        LambdaUpdateWrapper<Grade> lqw = new LambdaUpdateWrapper<Grade>().eq(Grade::getC_id,course.getC_id());
        gradeDao.delete(lqw);
        return courseDao.deleteById(course) > 0;
    }

    @Override
    public IPage<GradeInfo> Statistics(String CName,int currentPage,int pageSize,String condition) {
        List<GradeInfo> gradeInfoList = managerDao.gradeStatistics(CName,condition);
        List<GradeInfo> pageList = new ArrayList<>();
        IPage<GradeInfo> page = new Page<>(currentPage,pageSize);
        int curIdx = currentPage > 1 ? (currentPage - 1) * pageSize : 0;
        for (int i = 0; i < pageSize && curIdx + i < gradeInfoList.size(); i++) {
            pageList.add(gradeInfoList.get(curIdx + i));
        }
        page.setRecords(pageList);
        page.setTotal(gradeInfoList.size());
        return page;
    }
}
