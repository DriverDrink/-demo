package com.dhu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhu.dao.CourseDao;
import com.dhu.dao.GradeDao;
import com.dhu.dao.StudentDao;
import com.dhu.dao.TeacherDao;
import com.dhu.domain.Course;
import com.dhu.domain.Grade;
import com.dhu.domain.Student;
import com.dhu.domain.Teacher;
import com.dhu.domain.returnDomain.CourseGrade;
import com.dhu.domain.returnDomain.CourseGradeInfo;
import com.dhu.service.ITeacherService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ITeacherServiceImpl extends ServiceImpl<TeacherDao, Teacher> implements ITeacherService{

    @Autowired
    private TeacherDao teacherDao;

    @Autowired
    private GradeDao gradeDao;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private CourseDao courseDao;

    @Override
    public String login(Integer username, String password) {
        LambdaQueryWrapper<Teacher> lqw = new LambdaQueryWrapper<Teacher>().eq(Teacher::getT_id,username);
        Teacher teacher = teacherDao.selectOne(lqw);
        if(ObjectUtils.isEmpty(teacher))
            return "账户不存在";
        lqw.eq(Teacher::getT_password,password);
        teacher = teacherDao.selectOne(lqw);
        if(ObjectUtils.isEmpty(teacher))
            return "密码不正确，请重试";
        else
            return teacher.getT_name();
    }

    @Override
    public boolean alterPassword(Integer username, String newPassword) {
        Teacher teacher = new Teacher();
        LambdaUpdateWrapper<Teacher> luw = new LambdaUpdateWrapper<Teacher>().eq(Teacher::getT_id,username).set(Teacher::getT_password,newPassword);
        int i = teacherDao.update(teacher,luw);
        return i == 1;
    }

    @Override
    public List<Course> getMyCourse(Integer tid) {
        LambdaQueryWrapper<Course> lqw = new LambdaQueryWrapper<Course>().eq(Course::getT_id,tid);
        return courseDao.selectList(lqw);
    }

    @Override
    public IPage<CourseGrade> selectCourseGrade(Integer tid, Grade grade, int currentPage, int pageSize) {
        IPage<CourseGrade> page = new Page<>(currentPage,pageSize);
        if(grade.getC_id()!=null&&courseDao.selectOne(new LambdaQueryWrapper<Course>().eq(Course::getT_id,tid).eq(Course::getC_id,grade.getC_id()))==null)
            return page.setRecords(null);
        List<CourseGrade> courseGradeList = teacherDao.selectCGrade(tid, grade.getC_id(),grade.getS_id());
        List<CourseGrade> pageList = new ArrayList<>();
        int curIdx = currentPage > 1 ? (currentPage - 1) * pageSize : 0;
        for (int i = 0; i < pageSize && curIdx + i < courseGradeList.size(); i++) {
            pageList.add(courseGradeList.get(curIdx + i));
        }
        page.setRecords(pageList);
        page.setTotal(courseGradeList.size());
        return page;
    }

    @Override
    public CourseGrade getOne(Integer sid,Integer cid) {
        LambdaQueryWrapper<Grade> lqw = new LambdaQueryWrapper<Grade>().eq(Grade::getS_id,sid).eq(Grade::getC_id,cid);
        LambdaQueryWrapper<Student> lqw2 = new LambdaQueryWrapper<Student>().eq(Student::getS_id,sid);
        LambdaQueryWrapper<Course> lqw3 = new LambdaQueryWrapper<Course>().eq(Course::getC_id,cid);
        CourseGrade courseGrade = new CourseGrade();
        courseGrade.setS_id(sid);
        courseGrade.setS_class(studentDao.selectOne(lqw2).getS_class());
        courseGrade.setS_name(studentDao.selectOne(lqw2).getS_name());
        courseGrade.setScore(gradeDao.selectOne(lqw).getScore());
        courseGrade.setC_id(cid);
        courseGrade.setC_name(courseDao.selectOne(lqw3).getC_name());
        return courseGrade;
    }

    @Override
    public String insert(CourseGrade courseGrade, Integer tid) {
        if(studentDao.selectById(courseGrade.getS_id())==null)
            return "不存在的学号";
        if(!studentDao.selectById(courseGrade.getS_id()).getS_name().equals(courseGrade.getS_name()))
            return "错误的学生姓名";
        if(!studentDao.selectById(courseGrade.getS_id()).getS_class().equals(courseGrade.getS_class()))
            return "错误的学生班级";
        if(!courseDao.selectById(courseGrade.getC_id()).getT_id().equals(tid))
            return "您没有教授此门课";
        if(!courseDao.selectById(courseGrade.getC_id()).getC_name().equals(courseGrade.getC_name()))
            return "课程号和课程名不匹配";
        if(gradeDao.selectOne(new LambdaQueryWrapper<Grade>().eq(Grade::getS_id,courseGrade.getS_id()).eq(Grade::getC_id,courseGrade.getC_id()))!=null)
            return "成绩已经被录入！";
        Course course = courseDao.selectOne(new LambdaQueryWrapper<Course>().eq(Course::getC_name,courseGrade.getC_name()).ne(Course::getC_id,courseGrade.getC_id()));
        if(gradeDao.selectOne(new LambdaQueryWrapper<Grade>().eq(Grade::getS_id,courseGrade.getS_id()).eq(Grade::getC_id,course.getC_id()))!=null)
            return "学生未选修您的"+course.getC_name();
        Grade grade=new Grade();
        grade.setS_id(courseGrade.getS_id());
        grade.setC_id(courseGrade.getC_id());
        grade.setScore(courseGrade.getScore());
        int i = gradeDao.insert(grade);
        if(i==1) return "添加成功";
        else return "添加失败";
    }

    @Override
    public boolean delete(CourseGrade courseGrade) {
        LambdaQueryWrapper<Grade> lqw = new LambdaQueryWrapper<Grade>().eq(Grade::getS_id,courseGrade.getS_id()).eq(Grade::getC_id,courseGrade.getC_id());
        int i = gradeDao.delete(lqw);
        return i == 1;
    }

    @Override
    public boolean update(CourseGrade courseGrade) {
        LambdaUpdateWrapper<Grade> luw = new LambdaUpdateWrapper<Grade>().eq(Grade::getS_id,courseGrade.getS_id()).eq(Grade::getC_id,courseGrade.getC_id());
        luw.set(Grade::getScore,courseGrade.getScore());
        int i =gradeDao.update(new Grade(),luw);
        return i == 1;
    }

    @Override
    public CourseGradeInfo StatisticsT(Integer cid,Integer tid) {
        if(courseDao.selectOne(new LambdaQueryWrapper<Course>().eq(Course::getT_id,tid).eq(Course::getC_id,cid))==null)
            return null;
        return teacherDao.gradeStatisticsT(cid);
    }

    @Override
    public String[] excelToMysql(InputStream inputStream, Integer tid) throws IOException {
        Workbook workbook = WorkbookFactory.create(inputStream);
        inputStream.close();
        Sheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getLastRowNum()+1;
        Row row = sheet.getRow(0);
        int columns = row.getLastCellNum();
        int i,successNum=0;
        String[] importInfo=new String[rows+1];
        for(i=1;i<rows;i++){
            row = sheet.getRow(i);
            CourseGrade courseGrade= new CourseGrade();
            DataFormatter dataFormatter = new DataFormatter();
            String cell0 = dataFormatter.formatCellValue(row.getCell(0)).trim();
            courseGrade.setS_id(Integer.parseInt(cell0));
            String cell1 = dataFormatter.formatCellValue(row.getCell(1)).trim();
            courseGrade.setS_class(cell1);
            String cell2 = dataFormatter.formatCellValue(row.getCell(2)).trim();
            courseGrade.setS_name(cell2);
            String cell3 = dataFormatter.formatCellValue(row.getCell(3)).trim();
            courseGrade.setScore(Integer.parseInt(cell3));
            String cell4 = dataFormatter.formatCellValue(row.getCell(4)).trim();
            courseGrade.setC_name(cell4);
            String cell5 = dataFormatter.formatCellValue(row.getCell(5)).trim();
            courseGrade.setC_id(Integer.parseInt(cell5));
            String info = insert(courseGrade, tid);
            if(info.equals("成绩已经被录入！")){
                info = update(courseGrade)?"成绩曾被录入，已被修改":"成绩曾被录入，修改失败";
            }
            importInfo[i] = "第"+i+"条数据："+info;
            if(info.equals("添加成功")||info.equals("成绩曾被录入，已被修改")){
                successNum++;
            }
        }
        importInfo[i] ="文件共"+rows+"行，"+columns+"列";
        if(successNum < rows-1 && successNum > 0)
            importInfo[0] = "部分导入成功,共"+successNum+"条";
        else if(successNum == rows-1)
            importInfo[0] = "全部导入成功,共"+successNum+"条";
        else
            importInfo[0] = "数据导入失败";
        return importInfo;
    }
}
