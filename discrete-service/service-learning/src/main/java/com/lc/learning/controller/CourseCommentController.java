package com.lc.learning.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.common.utils.JwtUtils;
import com.lc.common.utils.ResultStatus;
import com.lc.common.utils.DiscreteResult;
import com.lc.learning.client.UcenterClient;
import com.lc.learning.entity.CourseComment;
import com.lc.learning.entity.User;
import com.lc.learning.service.CourseCommentService;
import com.lc.learning.service.CourseService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lc
 * @since 2021-03-17
 */
@RestController
@RequestMapping("/course-comment")
public class CourseCommentController {

    @Autowired
    private CourseCommentService courseCommentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UcenterClient ucenterClient;

    @PostMapping("/add")
    @ApiOperation("添加评论")
    public DiscreteResult addComment(@RequestBody CourseComment comment, HttpServletRequest request) {
        // 先判断是否登录
        String userId = JwtUtils.getUserIdByJwtToken(request);
        if (StringUtils.isEmpty(userId)) {
            return DiscreteResult.error().status(ResultStatus.UNAUTHORIZED.getCode()).message("请先登录！");
        }
        // 获取用户信息
        DiscreteResult result = ucenterClient.getUserInfo(userId);
        if (result.getSuccess()) {
            Map<String, Object> data = result.getData();
            Object userInfo = data.get("userInfo");
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.convertValue(userInfo, User.class);
            // 设置评论中员工相关信息
            comment.setUserId(user.getId());
            comment.setAvatar(user.getAvatar());
            comment.setUsername(user.getUsername());
            boolean success = courseCommentService.save(comment);
            // 评论数+1
            boolean commentSuccess = courseService.updateCommentNum(comment.getCourseId());
            return success && commentSuccess ? DiscreteResult.ok() : DiscreteResult.error().message("服务器错误！发表评论失败！");
        } else {
            return result;
        }
    }

    @GetMapping("/{page}/{limit}")
    @ApiOperation("分页查询评论信息")
    public DiscreteResult getCommentPage(@PathVariable(value = "page") Integer page,
                                         @PathVariable(value = "limit") Integer limit,
                                         String courseId) {
        // 执行条件分页查询
        Page<CourseComment> commentPage = new Page<>(page, limit);
        QueryWrapper<CourseComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        courseCommentService.page(commentPage, wrapper);
        // 设置查询结果相关参数，返回map
        HashMap<String, Object> map = new HashMap<>();
        map.put("rows", commentPage.getRecords());
        map.put("total", commentPage.getTotal());
        map.put("current", commentPage.getCurrent());
        map.put("size", commentPage.getSize());
        return DiscreteResult.ok().data(map);
    }
}

