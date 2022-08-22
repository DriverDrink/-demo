package com.dhu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dhu.dao.sqlSentence.Sql;
import com.dhu.domain.Student;
import com.dhu.domain.returnDomain.MyGrade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface StudentDao extends BaseMapper<Student> {

    @SelectProvider(type = Sql.class,method = "getAllGrade")
    List<MyGrade> getAllGrade(@Param("id") Integer id,@Param("term") String term);

}
