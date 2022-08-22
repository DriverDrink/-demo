package com.dhu.controller;

import com.dhu.controller.utils.DataR;
import com.dhu.domain.Course;
import com.dhu.domain.Student;
import com.dhu.domain.Teacher;
import com.dhu.service.IManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/managers")
public class MannagerController {
    @Autowired
    private IManagerService managerService;

    //登录
    @PostMapping("/login")
    public DataR login(@RequestParam("username") Integer username, @RequestParam("password") String password,HttpSession session){
        if(session.getAttribute("username") ==null) {
            String s = managerService.login(username,password);
            if(!s.equals("管理员账户不存在")&&!s.equals("密码不正确，请重试")) {
                session.setAttribute("username", username);
                session.setAttribute("password", password);
                return new DataR(true, s, "登录成功");
            }
            else return new DataR(s);
        }
        else return new DataR("此浏览器已经登陆了一个账户");
    }

    //修改密码
    @PutMapping("/alterPassword")
    public DataR alterPassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,HttpSession session){
        if(!session.getAttribute("password").equals(oldPassword)){
            return new DataR("旧密码错误");
        }
        else{
            if(!managerService.alterPassword(Integer.parseInt(session.getAttribute("username").toString()), newPassword)){
                return new DataR("新密码不能和旧密码一样");
            }
            else{
                session.setAttribute("password",newPassword);
                return new DataR(true,null,"更改成功");
            }
        }
    }

    //查询学生信息
    @GetMapping("/Stu")
    public DataR Stu(@RequestParam("currentPage") int currentPage, @RequestParam("pageSize") int pageSize,Student student) {
        return new DataR(true, managerService.Stu(currentPage,pageSize,student));
    }

    //查询单个学生信息
    @GetMapping("getOneStu")
    public DataR getOneStudent(@RequestParam("sid") Integer sid){
        return new DataR(true,managerService.getOneStu(sid));
    }

    //增加学生信息
    @PostMapping("/saveStu")
    public DataR saveStudent(@RequestBody Student student){
        Boolean flag = managerService.saveStu(student);
        return new DataR(flag,null,flag?"增加成功":"增加失败");
    }

    //修改学生信息
    @PutMapping("/updateStu")
    public DataR updateStudent(@RequestBody Student student){
        Boolean flag = managerService.updateStu(student);
        return new DataR(flag,null,flag?"修改成功":"修改失败");
    }

    //删除学生信息
    @DeleteMapping("/deleteStu")
    public DataR deleteStudent(@RequestBody Student student){
        Boolean flag = managerService.deleteStu(student);
        return new DataR(flag,null,flag?"删除成功":"删除失败");
    }

    //查询教师信息
    @GetMapping("/Tea")
    public DataR Tea(@RequestParam("currentPage") int currentPage, @RequestParam("pageSize") int pageSize, Teacher teacher) {
        return new DataR(true, managerService.Tea(currentPage,pageSize,teacher));
    }

    //查询单个教师信息
    @GetMapping("getOneTea")
    public DataR getOneTeacher(@RequestParam("tid") Integer tid){
        return new DataR(true,managerService.getOneTea(tid));
    }

    //增加教师信息
    @PostMapping("/saveTea")
    public DataR saveTeacher(@RequestBody Teacher teacher){
        Boolean flag = managerService.saveTea(teacher);
        return new DataR(flag,null,flag?"增加成功":"增加失败");
    }

    //修改教师信息
    @PutMapping("/updateTea")
    public DataR updateTeacher(@RequestBody Teacher teacher){
        Boolean flag = managerService.updateTea(teacher);
        return new DataR(flag,null,flag?"修改成功":"修改失败");
    }

    //删除教师信息
    @DeleteMapping("deleteTea")
    public DataR deleteTeacher(@RequestBody Teacher teacher){
        Boolean flag = managerService.deleteTea(teacher);
        return new DataR(flag,null,flag?"删除成功":"删除失败");
    }

    //查询课程信息
    @GetMapping("/Cou")
    public DataR Cou(@RequestParam("currentPage") int currentPage, @RequestParam("pageSize") int pageSize,Course course) {
        return new DataR(true, managerService.COU(currentPage,pageSize,course));
    }

    //查询单个课程信息
    @GetMapping("getOneCou")
    public DataR getOneCourse(@RequestParam("cid") Integer cid){
        return new DataR(true,managerService.getOneCou(cid));
    }

    //增加课程信息
    @PostMapping("/saveCou")
    public DataR saveCourse(@RequestBody Course course){
        Boolean flag = managerService.saveCou(course);
        return new DataR(flag,null,flag?"增加成功":"增加失败");
    }

    //修改课程信息
    @PutMapping("/updateCou")
    public DataR updateCourse(@RequestBody Course course){
        Boolean flag = managerService.updateCou(course);
        return new DataR(flag,null,flag?"修改成功":"修改失败");
    }

    //删除课程信息
    @DeleteMapping("/deleteCou")
    public DataR deleteCourse(@RequestBody Course course){
        Boolean flag = managerService.deleteCou(course);
        return new DataR(flag,null,flag?"删除成功":"删除失败");
    }

    //统计成绩
    @GetMapping("/gradeCount")
    public DataR gradeCount(@RequestParam("CName") String CName,@RequestParam("currentPage") int currentPage,@RequestParam("pageSize") int pageSize,@RequestParam("condition") String condition){
        return new DataR(true,managerService.Statistics(CName,currentPage,pageSize,condition));
    }

    //退出登录
    @PostMapping("/logout")
    public DataR logout(HttpSession session, SessionStatus sessionStatus){
        session.invalidate();
        sessionStatus.isComplete();
        return new DataR(true,null,"成功退出登录");
    }
}
