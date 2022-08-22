package com.dhu.dao;


import com.dhu.domain.Course;
import com.dhu.domain.Grade;
import com.dhu.domain.Student;
import com.dhu.domain.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class StudentDaoTest {

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private ManagerDao managerDao;
    @Autowired
    private CourseDao courseDao;

    @Autowired
    private GradeDao gradeDao;

    @Test
    void testSave() {
        List<Student> selectList = studentDao.selectList(null);
        for (int i = 0; i < 100; i++) {
            Grade grade = new Grade();
            grade.setS_id(selectList.get(i).getS_id());
            grade.setC_id(i>49?30434:30666);
            int x=(int)(Math.random()*100);
            if(x<40) x+=45;
            grade.setScore(x);
            gradeDao.insert(grade);
        }
    }
}

//        Grade grade = new Grade();
//        grade.setS_id(student.getS_id());
//        grade.setC_id(1);
//        grade.setScore(95);
//        gradeDao.insert(grade);

