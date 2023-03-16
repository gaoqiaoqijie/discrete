package com.lc.video.controller;

import com.lc.common.utils.DiscreteResult;
import com.lc.video.service.VideoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author ：lc
 * @date ：Created in 2021/2/28 19:07
 * @modified By：
 */
@RestController
@RequestMapping("/vod")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("/upload")
    @ApiOperation("视频上传")
    public DiscreteResult uploadVideo(MultipartFile file) {
        String videoSourceId = videoService.uploadVideo(file);
        return DiscreteResult.ok().data("videoSourceId", videoSourceId);
    }

    @GetMapping("/{videoSourceId}")
    @ApiOperation("根据云端videoId来获取视频时长及大小信息")
    public DiscreteResult getVideoInfo(@PathVariable(value = "videoSourceId") String videoSourceId) {
        Map<String, Object> infoMap = videoService.getVideoInfo(videoSourceId);
        Float duration = (Float) infoMap.get("duration");
        Long size = (Long) infoMap.get("size");
        return DiscreteResult.ok().data("duration", duration).data("size", size);
    }

    @GetMapping("/playAuth/{videoSourceId}")
    @ApiOperation("获取视频播放凭证")
    public DiscreteResult getPlayAuthById(@PathVariable(value = "videoSourceId") String videoSourceId) {
        String auth = videoService.getPlayAuthById(videoSourceId);
        return DiscreteResult.ok().data("playAuth", auth);
    }

    @DeleteMapping("/{videoSourceId}")
    @ApiOperation("删除云端视频")
    public DiscreteResult deleteVideo(@PathVariable(value = "videoSourceId") String videoSourceId) {
        boolean success = videoService.deleteVideo(videoSourceId);
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器错误！删除失败！");
    }
}
