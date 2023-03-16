package com.lc.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lc.learning.entity.UserVideo;
import com.lc.learning.mapper.UserVideoMapper;
import com.lc.learning.service.UserCourseService;
import com.lc.learning.service.UserVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.learning.service.VideoService;
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
public class UserVideoServiceImpl extends ServiceImpl<UserVideoMapper, UserVideo> implements UserVideoService {


    @Autowired
    private VideoService videoService;

    @Autowired
    private UserCourseService userCourseService;

    @Override
    public boolean recordAllTime(String userId, String videoId, Float allTime) {
        // 先查询出上播放的总时长（员工重复学习也会添加学习时长）
        QueryWrapper<UserVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("video_id", videoId);
        wrapper.eq("user_id", userId);
        UserVideo userVideo = baseMapper.selectOne(wrapper);
        int line = 0;
        if (userVideo == null) {
            // 查不到记录代表是第一次观看，就插入数据即可
            UserVideo sv = new UserVideo();
            sv.setVideoId(videoId);
            sv.setUserId(userId);
            sv.setDuration(videoService.getDuration(videoId));
            sv.setLearningTime(allTime);
            line = baseMapper.insert(sv);
        } else {
            // 找到的话就把当前的总学习时长加到上次的学习时长上
            userVideo.setLearningTime(userVideo.getLearningTime() + allTime);
            line = baseMapper.updateById(userVideo);
        }
        return line > 0;
    }

    @Override
    public boolean recordLastTime(String userId, String videoId, Float lastTime) {
        // 这个直接更新播放进度就行
        UserVideo userVideo = new UserVideo();
        userVideo.setLastTime(lastTime);
        QueryWrapper<UserVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("video_id", videoId);
        wrapper.eq("user_id", userId);
        int line = baseMapper.update(userVideo, wrapper);
        return line > 0;
    }

    @Override
    public Float getLastTime(String userId, String videoId) {
        // 条件查询指定字段（last_time）
        QueryWrapper<UserVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("video_id", videoId);
        wrapper.eq("user_id", userId);
        UserVideo userVideo = baseMapper.selectOne(wrapper.select("last_time"));
        return userVideo != null ? userVideo.getLastTime() : null;
    }

    @Override
    public Integer getLearning(String userId) {
        return baseMapper.getLearning(userId);
    }

    @Override
    public boolean accomplishVideo(String courseId, String videoId, String userId) {
        QueryWrapper<UserVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("video_id", videoId);
        wrapper.eq("user_id", userId);
        UserVideo userVideo = baseMapper.selectOne(wrapper);
        // 如果这个小节已经完成了，就不用设置了
        if (userVideo.getAccomplishFlag() == 1) {
            return true;
        } else {
            // 更新对应课程已学习的小节数
            boolean success = userCourseService.updateLearningVideoNum(courseId, userId);
            if (success) {
                // 更新小节，设置小节已完成
                userVideo.setAccomplishFlag(1);
                return baseMapper.updateById(userVideo) > 0;
            }
        }
        return false;
    }
}
