package com.lc.learning.controller;


import com.lc.common.utils.JwtUtils;
import com.lc.common.utils.ResultStatus;
import com.lc.common.utils.DiscreteResult;
import com.lc.learning.service.UserVideoService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lc
 * @since 2021-03-11
 */
@RestController
@RequestMapping("/user-video")
public class UserVideoController {

    @Autowired
    private UserVideoService userVideoService;

    @GetMapping("/reocrd/{videoId}")
    @ApiOperation("记录员工观看小节的总时长")
    public DiscreteResult recordAllTime(@PathVariable(value = "videoId") String videoId,
                                        @RequestParam(value = "allTime") Float allTime,
                                        HttpServletRequest request) {
        // 先检查是否登录，防止登录状态失效导致学习时长没有记录上
        String userId = JwtUtils.getUserIdByJwtToken(request);
        if (StringUtils.isEmpty(userId)) {
            return DiscreteResult.error().status(ResultStatus.UNAUTHORIZED.getCode()).message("登录状态失效，请重新登录！");
        } else {
            boolean success = userVideoService.recordAllTime(userId, videoId, allTime);
            return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器错误，学习时长记录失败！");
        }
    }

    @GetMapping("/last/{videoId}")
    @ApiOperation("记录员工观看小节上次播放记录")
    public DiscreteResult lastVideoTime(@PathVariable(value = "videoId") String videoId,
                                        @RequestParam(value = "lastTime") Float lastTime,
                                        HttpServletRequest request) {
        // 先检查是否登录，防止登录状态失效导致播放记录未保存
        String userId = JwtUtils.getUserIdByJwtToken(request);
        if (StringUtils.isEmpty(userId)) {
            return DiscreteResult.error().status(ResultStatus.UNAUTHORIZED.getCode()).message("登录状态失效，请重新登录！");
        } else {
            boolean success = userVideoService.recordLastTime(userId, videoId, lastTime);
            return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器错误，播放记录失败！");
        }
    }

    @GetMapping("/progress/{videoId}")
    @ApiOperation("获取员工上次播放进度")
    public DiscreteResult getLastTime(@PathVariable(value = "videoId") String videoId,
                                      HttpServletRequest request) {
        // 先检查是否登录，防止登录状态失效导致播放记录未保存
        String userId = JwtUtils.getUserIdByJwtToken(request);
        if (StringUtils.isEmpty(userId)) {
            return DiscreteResult.error().status(ResultStatus.UNAUTHORIZED.getCode()).message("登录状态失效，请重新登录！");
        } else {
            Float lastTime = userVideoService.getLastTime(userId, videoId);
            // 不为空就返回具体时间，为空（以前没看过，查不到记录）就从0开始看（返回0）
            return lastTime != null ? DiscreteResult.ok().data("lastTime", lastTime) : DiscreteResult.ok().data("lastTime", 0);
        }
    }

    @GetMapping("/accomplish/{courseId}/{videoId}")
    @ApiOperation("小节观看完毕设置小节完成字段为1")
    public DiscreteResult accomplishVideo(@PathVariable("videoId") String videoId,
                                          @PathVariable("courseId") String courseId,
                                          HttpServletRequest request) {
        // 先检查是否登录，防止登录状态失效导致观看未完成
        String userId = JwtUtils.getUserIdByJwtToken(request);
        if (StringUtils.isEmpty(userId)) {
            return DiscreteResult.error().status(ResultStatus.UNAUTHORIZED.getCode()).message("登录状态失效，请重新登录！");
        } else {
            boolean success = userVideoService.accomplishVideo(courseId, videoId, userId);
            return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器错误，小节观看完成记录失败！");
        }
    }

    @GetMapping("/time/{userId}")
    @ApiOperation("获取指定员工学习总时长")
    public DiscreteResult getLearningTime(@PathVariable("userId") String userId) {
        return DiscreteResult.ok().data("learningTime", userVideoService.getLearning(userId));
    }
}

