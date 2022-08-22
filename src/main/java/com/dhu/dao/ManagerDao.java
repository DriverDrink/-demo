package com.dhu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dhu.dao.sqlSentence.Sql;
import com.dhu.domain.Manager;
import com.dhu.domain.returnDomain.GradeInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ManagerDao extends BaseMapper<Manager> {

    @SelectProvider(type = Sql.class,method = "gradeStatistics")
    List<GradeInfo> gradeStatistics(@Param("c_name") String c_name,String condition);

}
