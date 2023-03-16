package com.lc.learning.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lc.learning.entity.UserCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.learning.entity.vo.JoinCourseVo;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lc
 * @since 2021-03-11
 */
public interface UserCourseMapper extends BaseMapper<UserCourse> {


    /**
     * 查询指定职工参与的课程
     * @param page 分页对象
     * @param userId 职工id
     * @return JoinCourseVo集合
     */
    IPage<JoinCourseVo> getJoinCourses(IPage<JoinCourseVo> page, String userId);
}
