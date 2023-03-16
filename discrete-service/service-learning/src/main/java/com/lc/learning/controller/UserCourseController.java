package com.lc.learning.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lc.common.utils.JwtUtils;
import com.lc.common.utils.ResultStatus;
import com.lc.common.utils.DiscreteResult;
import com.lc.learning.entity.vo.JoinCourseVo;
import com.lc.learning.service.UserCourseService;
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
@RequestMapping("/user-course")
public class UserCourseController {

    @Autowired
    private UserCourseService userCourseService;

    @GetMapping("/study/{courseId}")
    @ApiOperation("查询指定用户是否参与了本课程的学习")
    public DiscreteResult isStudyCourse(@PathVariable(value = "courseId") String courseId,
                                        @RequestParam(value = "userId", required = false) String userId) {
        boolean result = userCourseService.isStudyCourse(userId, courseId);
        return DiscreteResult.ok().data("isStudy", result);
    }

    @GetMapping("/{page}/{limit}")
    @ApiOperation("查询指定用户参与的课程信息")
    public DiscreteResult getJoinCourse(HttpServletRequest request,
                                        @PathVariable(value = "page") Long page,
                                        @PathVariable(value = "limit") Long limit) {
        // 判断是否登录
        String userId = JwtUtils.getUserIdByJwtToken(request);
        if (StringUtils.isEmpty(userId)) {
            return DiscreteResult.ok().status(ResultStatus.UNAUTHORIZED.getCode()).message("请先登录！");
        } else {
            Page<JoinCourseVo> pageParam = new Page<>(page, limit);
            userCourseService.getJoinCourse(pageParam, userId);
            return DiscreteResult.ok().data("total", pageParam.getTotal()).data("rows", pageParam.getRecords());
        }
    }

    @GetMapping("/progress/{userId}/{courseId}")
    @ApiOperation("获取指定用户观看指定课程的播放进度(未测试)")
    public DiscreteResult getPercent(@PathVariable("userId") String userId,
                                     @PathVariable("courseId") String courseId) {
        Double percentage = userCourseService.getPercentage(userId, courseId);
        return DiscreteResult.ok().data("percentage", percentage);
    }
}

