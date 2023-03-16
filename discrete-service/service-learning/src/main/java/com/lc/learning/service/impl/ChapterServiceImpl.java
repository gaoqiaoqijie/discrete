package com.lc.learning.service.impl;

/**
 * @author luochao
 * @Classname ChapterServiceImpl
 * @Description TODO
 * @Date 2023/3/5 16:22
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lc.common.exception.TrainingException;
import com.lc.learning.entity.Chapter;
import com.lc.learning.entity.UserVideo;
import com.lc.learning.entity.Video;
import com.lc.learning.entity.vo.TreeChapter;
import com.lc.learning.entity.vo.TreeVideo;
import com.lc.learning.mapper.ChapterMapper;
import com.lc.learning.mapper.UserVideoMapper;
import com.lc.learning.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.learning.service.UserVideoService;
import com.lc.learning.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author houcheng
 * @since 2021-02-25
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserVideoMapper userVideoMapper;

    @Override
    public List<TreeChapter> getTreeChapter(String courseId) {
        // 先查出此课程下所有章节数据
        QueryWrapper<Chapter> chapterQuery = new QueryWrapper<>();
        chapterQuery.eq("course_id", courseId);
        List<Chapter> chapters = baseMapper.selectList(chapterQuery);

        // 根据课程id查出此课程下的所有小节
        QueryWrapper<Video> videoQuery = new QueryWrapper<>();
        videoQuery.eq("course_id", courseId);
        List<Video> videos = videoService.list(videoQuery);
        // 开始分类
        List<TreeChapter> treeChapters = new ArrayList<>();
        // 循环章节，根据章节id封装小节
        for (Chapter chapter : chapters){
            TreeChapter treeChapter = new TreeChapter();
            List<TreeVideo> treeVideos = new ArrayList<>();
            for (Video video : videos){
                if (video.getChapterId().equals(chapter.getId())){
                    TreeVideo treeVideo = new TreeVideo();

                    // 设置小节信息
                    treeVideo.setId(video.getId());
                    treeVideo.setTitle(video.getTitle());
                    // 此章节id下的所有小节依次放入list中，循环完毕统一放入此章节树对象中
                    treeVideos.add(treeVideo);
                }
            }
            // 设置章节信息
            treeChapter.setId(chapter.getId());
            treeChapter.setTitle(chapter.getTitle());
            treeChapter.setChildren(treeVideos);
            // 往结果集中放入章节信息
            treeChapters.add(treeChapter);
        }
        return treeChapters;
    }

    @Override
    public List<TreeChapter> getTreeChapter(String userId, String courseId) {
        // 先查出此课程下所有章节数据
        QueryWrapper<Chapter> chapterQuery = new QueryWrapper<>();
        chapterQuery.eq("course_id", courseId);
        List<Chapter> chapters = baseMapper.selectList(chapterQuery);

        // 根据课程id查出此课程下的所有小节
        QueryWrapper<Video> videoQuery = new QueryWrapper<>();
        videoQuery.eq("course_id", courseId);
        List<Video> videos = videoService.list(videoQuery);
        // 开始分类
        List<TreeChapter> treeChapters = new ArrayList<>();
        // 循环章节，根据章节id封装小节
        for (Chapter chapter : chapters){
            TreeChapter treeChapter = new TreeChapter();
            List<TreeVideo> treeVideos = new ArrayList<>();
            for (Video video : videos){
                if (video.getChapterId().equals(chapter.getId())){
                    TreeVideo treeVideo = new TreeVideo();

                    // 设置小节信息
                    treeVideo.setId(video.getId());
                    treeVideo.setTitle(video.getTitle());
                    // 查询是否完成了对应章节
                    QueryWrapper<UserVideo> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("user_id", userId).eq("video_id", video.getId());
                    UserVideo userVideo = userVideoMapper.selectOne(queryWrapper);
                    if (null == userVideo) {
                        treeVideo.setAccomplishFlag(0);
                    } else {
                        treeVideo.setAccomplishFlag(userVideo.getAccomplishFlag());
                    }
                    // 此章节id下的所有小节依次放入list中，循环完毕统一放入此章节树对象中
                    treeVideos.add(treeVideo);
                }
            }
            // 设置章节信息
            treeChapter.setId(chapter.getId());
            treeChapter.setTitle(chapter.getTitle());
            treeChapter.setChildren(treeVideos);
            // 往结果集中放入章节信息
            treeChapters.add(treeChapter);
        }
        return treeChapters;
    }

    @Override
    public boolean deleteChapter(String id) {
        // 先删除此章节下得小节信息
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("chapter_id", id);
        List<Video> list = videoService.list(videoQueryWrapper);
        // 默认为true，如果下面没有小节信息就直接返回true了
        boolean success = true;
        for (Video video : list) {
            success = videoService.deleteVideo(video);
        }
        // 删除章节信息
        // 为什么success在前？因为如果小节数据删除出现问题了，这个就不会执行后面的了，直接返回false
        return  success && baseMapper.deleteById(id)>0;
    }

    @Override
    public boolean deleteChapterByCourseId(String courseId) {
        try {
            QueryWrapper<Chapter> wrapper = new QueryWrapper<>();
            wrapper.eq("course_id",courseId);
            baseMapper.delete(wrapper);
            return true;
        } catch (Exception e) {
            throw new TrainingException(500, "服务器错误，章节信息删除失败！");
        }
    }
}