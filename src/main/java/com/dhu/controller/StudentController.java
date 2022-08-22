package com.dhu.controller;

import com.dhu.controller.utils.DataR;
import com.dhu.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private IStudentService studentService;

    //登录
    @PostMapping("/login")
    public DataR login(@RequestParam("username") Integer username, @RequestParam("password") String password,HttpSession session){
        if(session.getAttribute("username") == null) {
            String s = studentService.login(username,password);
            if(!s.equals("学号不存在")&&!s.equals("密码不正确，请重试")) {
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
            if(!studentService.alterPassword(Integer.parseInt(session.getAttribute("username").toString()), newPassword)){
                return new DataR("新密码不能和旧密码一样");
            }
            else{
                session.setAttribute("password",newPassword);
                return new DataR(true,null,"更改成功");
            }
        }
    }

    //查询成绩
    @GetMapping("/myGrade")
    public DataR selectGrade(@RequestParam("currentPage") int currentPage,@RequestParam("pageSize") int pageSize,@RequestParam("term") String term,HttpSession session){
        Integer id = Integer.parseInt(session.getAttribute("username").toString());
        return new DataR(true, studentService.getSelfGrade(id,term,currentPage,pageSize));
    }

    //退出登录
    @PostMapping("/logout")
    public DataR logout(HttpSession session, SessionStatus sessionStatus){
        session.invalidate();
        sessionStatus.isComplete();
        return new DataR(true,null,"成功退出登录");
    }

}
