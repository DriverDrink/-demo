package com.dhu.dao.sqlSentence;

import org.apache.ibatis.annotations.Param;

public class Sql {

    //ManagerDao
    public String gradeStatistics(@Param("c_name") String c_name,String condition){
        String sql =" select " ;

            sql +=" s_department, ";
        if(!condition.equals("department"))
            sql +=" s_major, ";
        if(!condition.equals("major")&&!condition.equals("department"))
            sql +=" s_class, ";

        sql +=" sum(case when score>=60 then 0 else 1 end) as failCount, ";
        sql +=" min(score) as lowestScore, ";
        sql +=" max(score) as highestScore, ";
        sql +=" avg(score) as averageScore ";
        sql +=" from student,grade,course ";
        sql +=" where student.s_id=grade.s_id and grade.c_id=course.c_id ";
        sql +=" and c_name=#{c_name} ";

            sql +=" group by s_department ";
        if(!condition.equals("department"))
            sql +=" ,s_major ";
        if(!condition.equals("major")&&!condition.equals("department"))
            sql +=" ,s_class ";

            sql +=" order by s_department ";
        if(!condition.equals("department"))
            sql +=" ,s_major ";
        if(!condition.equals("major")&&!condition.equals("department"))
            sql +=" ,s_class ";

        return sql;
    }

    //TeacherDao
    public String selectCGrade(@Param("tid") Integer tid, @Param("cid")Integer cid, @Param("sid") Integer sid){
        String sql=" select " +
                " student.s_id,s_class,s_name,score,c_name,course.c_id " +
                " from student,grade,course,teacher " +
                " where teacher.t_id=#{tid} ";
        if(cid != null)     sql+=" and course.c_id=#{cid} ";
        if(sid != null)     sql+=" and student.s_id like concat('%',#{sid},'%')";
               sql+= " and student.s_id=grade.s_id and grade.c_id=course.c_id and course.t_id=teacher.t_id ";
        return sql;
    }

    public String gradeStatisticsT(@Param("c_id") Integer c_id){
        String sql =" select " +
                "sum(case when score>=60 then 0 else 1 end) as failCount," +
                "sum(case when score between 60 and 69 then 1 else 0 end) as B60_69Count," +
                "sum(case when score between 70 and 79 then 1 else 0 end) as B70_79Count," +
                "sum(case when score between 80 and 89 then 1 else 0 end) as B80_89Count," +
                "sum(case when score between 90 and 100 then 1 else 0 end) as B90_100Count," +
                "min(score) as lowestScore," +
                "max(score) as highestScore," +
                "avg(score) as averageScore ";
        sql +=" from student,grade,course ";
        sql +=" where student.s_id=grade.s_id and grade.c_id=course.c_id ";
        sql +=" and grade.c_id=#{c_id} ";
        return sql;
    }

    //StudentDao
    public String getAllGrade(@Param("id") Integer id,@Param("term") String term){
        String sql =" select " +
                " c_name,score,credit,term " +
                " from student,grade,course " +
                " where student.s_id=#{id} and course.term like concat('%',#{term},'%') " +
                " and student.s_id=grade.s_id and grade.c_id=course.c_id ";
        return sql;
    }

}
