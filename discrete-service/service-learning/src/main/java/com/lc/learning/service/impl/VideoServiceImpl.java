package com.lc.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lc.common.exception.TrainingException;
import com.lc.learning.client.VodClient;
import com.lc.learning.entity.Video;
import com.lc.learning.entity.vo.VideoVo;
import com.lc.learning.mapper.VideoMapper;
import com.lc.learning.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lc
 * @since 2021-02-26
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private VodClient vodClient;

    @Override
    public Float getDuration(String videoId) {
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.eq("id", videoId);
        Video video = baseMapper.selectOne(wrapper.select("duration"));
        if (video != null) {
            return video.getDuration();
        } else {
            throw new TrainingException(500, "未查询到指定小节的时长！");
        }
    }

    @Override
    public boolean deleteVideo(Video video) {
        // 删除视频
        Boolean success = vodClient.deleteVideo(video.getVideoSourceId()).getSuccess();
        // 可能章节下没有数据，所以不返回影响的行数，直接执行不出异常即可
        baseMapper.deleteById(video.getId());
        return success;
    }

    @Override
    public boolean deleteVideoByCourseId(String courseId) {
        boolean success = true;
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        List<Video> videos = baseMapper.selectList(wrapper);
        for (Video video : videos) {
            if(!this.deleteVideo(video)){
                success = false;
            }
        }
        return success;
    }

    @Override
    public boolean updateVideo(VideoVo videoVo) {
        Video video = new Video();
        BeanUtils.copyProperties(videoVo, video);
        int line = baseMapper.updateById(video);
        return line > 0;
    }
}
