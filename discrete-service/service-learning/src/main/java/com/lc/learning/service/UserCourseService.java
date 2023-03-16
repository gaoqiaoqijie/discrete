package com.lc.learning.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lc.learning.entity.UserCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.learning.entity.vo.JoinCourseVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lc
 * @since 2021-03-11
 */
public interface UserCourseService extends IService<UserCourse> {


    /**
     * 查询指定用户是否学习了指定课程
     * @param userId 用户id
     * @param courseId 课程id
     * @return 是否学习
     */
    boolean isStudyCourse(String userId, String courseId);

    /**
     * 查询指定用户参与的课程
     * @param userId 用户
     * @return 分页对象
     */
    IPage<JoinCourseVo> getJoinCourse(Page<JoinCourseVo> voPage,String userId);

    /**
     * 获取指定用户指定课程的模仿进度
     * @param userId 用户id
     * @param courseId 课程id
     * @return 播放进度
     */
    Double getPercentage(String userId, String courseId);

    /**
     * 更新已观看小节数
     * @param courseId 课程id
     * @param userId 职工id
     * @return 是否成功
     */
    boolean updateLearningVideoNum(String courseId, String userId);
}
