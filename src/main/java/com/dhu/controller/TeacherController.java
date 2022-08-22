package com.dhu.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dhu.controller.utils.DataR;
import com.dhu.domain.Grade;
import com.dhu.domain.Teacher;
import com.dhu.domain.returnDomain.CourseGrade;
import com.dhu.domain.returnDomain.CourseGradeInfo;
import com.dhu.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private ITeacherService teacherService;

    //登录
    @PostMapping("/login")
    public DataR login(@RequestParam("username") Integer username,@RequestParam("password") String password,HttpSession session){
        if(session.getAttribute("username") ==null) {
            String s = teacherService.login(username,password);
            if(!s.equals("账户不存在")&&!s.equals("密码不正确，请重试")) {
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
            if(!teacherService.alterPassword(Integer.parseInt(session.getAttribute("username").toString()), newPassword)){
                return new DataR("新密码不能和旧密码一样");
            }
            else{
                session.setAttribute("password",newPassword);
                return new DataR(true,null,"更改成功");
            }
        }
    }

    //修改电话
    @PutMapping("/alterTel")
    public DataR alterTel(@RequestParam("newTel") String newTel,HttpSession session){
        Integer tid = Integer.parseInt(session.getAttribute("username").toString());
        LambdaUpdateWrapper<Teacher> luw= new LambdaUpdateWrapper<Teacher>().eq(Teacher::getT_id,tid).set(Teacher::getTelephone,newTel);
        return new DataR(teacherService.update(luw));
    }

    //查询自己所教课程
    @GetMapping("/myCourse")
    public DataR selectMyCourse(HttpSession session){
        Integer tid = Integer.parseInt(session.getAttribute("username").toString());
        return new DataR(true,teacherService.getMyCourse(tid));
    }

    //查询成绩
    @GetMapping("/courseGrade")
    public DataR selectGrade(@RequestParam("currentPage") int currentPage, @RequestParam("pageSize") int pageSize, Grade grade,HttpSession session){
        System.out.println(grade);
        Integer tid = Integer.parseInt(session.getAttribute("username").toString());
        IPage<CourseGrade> page =teacherService.selectCourseGrade(tid,grade,currentPage,pageSize);
        if(page.getRecords()==null)
            return new DataR("您没有教授此门课");
        else
            return new DataR(true, page);
    }

    //查询单体成绩
    @GetMapping("/getOneGrade")
    public DataR selectOneGrade(@RequestParam("sid") Integer sid,@RequestParam("cid") Integer cid){
        return new DataR(true,teacherService.getOne(sid,cid));
    }

    //添加成绩
    @PostMapping("/insertGrade")
    public DataR insertGrade(@RequestBody CourseGrade courseGrade,HttpSession session){
        Integer tid = Integer.parseInt(session.getAttribute("username").toString());
        if(teacherService.insert(courseGrade,tid).equals("添加成功"))
            return new DataR(true);
        else
            return new DataR(teacherService.insert(courseGrade,tid));
    }

    //删除成绩
    @DeleteMapping("deleteGrade")
    public DataR deleteGrade(@RequestBody CourseGrade courseGrade){
        Boolean flag = teacherService.delete(courseGrade);
        return new DataR(flag,null,flag?"删除成功":"删除失败");
    }

    //修改成绩
    @PutMapping("updateGrade")
    public DataR updateGrade(@RequestBody CourseGrade courseGrade){
        Boolean flag = teacherService.update(courseGrade);
        return new DataR(flag,null,flag?"修改成功":"修改失败");
    }

    //统计成绩
    @GetMapping("/gradeDistribution")
    public DataR getDistribution(@RequestParam("cid") Integer cid,HttpSession session){
        Integer tid = Integer.parseInt(session.getAttribute("username").toString());
        CourseGradeInfo courseGradeInfo = teacherService.StatisticsT(cid,tid);
        if(courseGradeInfo==null)
            return new DataR("您没有教授此门课");
        else
            return new DataR(true,courseGradeInfo);
    }

    //退出登录
    @PostMapping("/logout")
    public DataR logout(HttpSession session, SessionStatus sessionStatus){
        session.invalidate();
        sessionStatus.isComplete();
        return new DataR(true,null,"成功退出登录");
    }

    //导入Excel文件
    @PostMapping("/ExcelImport")
    public DataR ExcelGradeImport (@RequestParam("file") MultipartFile file,@RequestParam("tid") Integer tid){
        String name = file.getOriginalFilename();
        try{
            String[] message = teacherService.excelToMysql(file.getInputStream(),tid);
            if(!message[0].equals("数据导入失败"))
                return new DataR(true,message,name+message[0]);
            else
                return new DataR(false,message,name+message[0]);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new DataR("文件内容发生错误，导致无法正确导入");
        }
    }
}
