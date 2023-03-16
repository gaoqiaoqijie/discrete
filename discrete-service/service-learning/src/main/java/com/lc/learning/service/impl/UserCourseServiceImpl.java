package com.lc.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lc.learning.entity.UserCourse;
import com.lc.learning.entity.vo.JoinCourseVo;
import com.lc.learning.mapper.UserCourseMapper;
import com.lc.learning.service.CourseService;
import com.lc.learning.service.UserCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lc
 * @since 2021-03-11
 */
@Service
public class UserCourseServiceImpl extends ServiceImpl<UserCourseMapper, UserCourse> implements UserCourseService {


    @Override
    public boolean isStudyCourse(String userId, String courseId) {
        QueryWrapper<UserCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("course_id", courseId);
        // 查count，能查到记录就行，不在乎查出来记录是啥情况
        Integer result = baseMapper.selectCount(wrapper);
        return result > 0;
    }

    @Override
    public IPage<JoinCourseVo> getJoinCourse(Page<JoinCourseVo> voPage, String userId) {
        return baseMapper.getJoinCourses(voPage, userId);
    }

    @Override
    public Double getPercentage(String userId, String courseId) {
        QueryWrapper<UserCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("course_id", courseId);
        UserCourse userCourse = baseMapper.selectOne(wrapper);
        return (userCourse.getLearningVideoNum() * 1.0 / userCourse.getAllVideoNum());
    }

    @Override
    public boolean updateLearningVideoNum(String courseId, String userId) {
        QueryWrapper<UserCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.eq("user_id", userId);
        UserCourse userCourse = baseMapper.selectOne(wrapper);
        // 已学习小节数+1
        userCourse.setLearningVideoNum(userCourse.getLearningVideoNum() + 1);
        // 如果已学习小节和总共的小节数相同，就标记已经完成此课程
        if (userCourse.getLearningVideoNum().equals(userCourse.getAllVideoNum())) {
            userCourse.setAccomplishFlag(1);
        }
        return baseMapper.updateById(userCourse) > 0;
    }
}
