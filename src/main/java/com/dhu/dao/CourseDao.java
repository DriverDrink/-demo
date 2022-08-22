package com.dhu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dhu.domain.Course;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseDao extends BaseMapper<Course> {
}
