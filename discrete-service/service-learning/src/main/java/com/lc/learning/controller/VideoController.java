package com.lc.learning.controller;


import com.alibaba.excel.util.StringUtils;
import com.lc.common.utils.JwtUtils;
import com.lc.common.utils.ResultStatus;
import com.lc.common.utils.DiscreteResult;
import com.lc.learning.client.VodClient;
import com.lc.learning.entity.Video;
import com.lc.learning.entity.vo.VideoVo;
import com.lc.learning.service.VideoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lc
 * @since 2021-02-26
 */
@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private VodClient vodClient;

    @GetMapping("{id}")
    @ApiOperation("根据小节id获取小节信息")
    public DiscreteResult getVideo(@PathVariable(value = "id") String id) {
        Video video = videoService.getById(id);
        return DiscreteResult.ok().data("video", video);
    }

    @GetMapping("/duration/{id}")
    @ApiOperation("获取小节时长")
    public DiscreteResult getDuration(@PathVariable(value = "id") String id) {
        Float duration = videoService.getDuration(id);
        return DiscreteResult.ok().data("duration", duration);
    }

    @GetMapping("/playAuth/{id}")
    @ApiOperation("根据小节id获取小节视频播放凭证")
    public DiscreteResult getPlayAuthById(@PathVariable("id") String id, HttpServletRequest request) {
        // 先判断是否登录，未登录不允许播放视频
        String userId = JwtUtils.getUserIdByJwtToken(request);
        if (StringUtils.isEmpty(userId)) {
            return DiscreteResult.ok().status(ResultStatus.UNAUTHORIZED.getCode()).message("请先登录！");
        } else {
            Video video = videoService.getById(id);
            DiscreteResult result = vodClient.getPlayAuthById(video.getVideoSourceId());
            result.data("videoSourceId", video.getVideoSourceId());
            return result;
        }
    }

    @PostMapping("/add")
    @ApiOperation("添加小节信息")
    public DiscreteResult addVideo(@RequestBody VideoVo videoVo) {
        Video video = new Video();
        BeanUtils.copyProperties(videoVo, video);
        DiscreteResult videoInfo = vodClient.getVideoInfo(videoVo.getVideoSourceId());
        Map<String, Object> data = videoInfo.getData();
        Float duration =Float.parseFloat(data.get("duration").toString());
        Long size = Long.valueOf(data.get("size").toString());
        video.setDuration(duration);
        video.setSize(size);
        boolean success = videoService.save(video);
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器出现错误，请重试！");
    }

    @PostMapping("/update")
    @ApiOperation("更新小节信息")
    public DiscreteResult updateVideo(@RequestBody VideoVo videoVo) {
        boolean success = videoService.updateVideo(videoVo);
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器出现错误，更新失败！");
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除小节信息")
    public DiscreteResult deleteVideo(@PathVariable(value = "id") String id) {
        boolean success = videoService.deleteVideo(videoService.getById(id));
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器出现错误，删除失败！");
    }

    @GetMapping("/test")
    @ApiOperation("测试feign")
    public DiscreteResult getVideoInfo(String videoSourceId) {
        DiscreteResult videoInfo = vodClient.getVideoInfo(videoSourceId);
        Map<String, Object> data = videoInfo.getData();
        Float duration =Float.parseFloat(data.get("duration").toString());
        Long size = Long.valueOf(data.get("size").toString());
        return DiscreteResult.ok().data("duration", duration).data("size", size);
    }
}

