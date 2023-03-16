package com.lc.qa.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.common.utils.JwtUtils;
import com.lc.common.utils.ResultStatus;
import com.lc.common.utils.DiscreteResult;
import com.lc.qa.client.UcenterClient;
import com.lc.qa.entity.Question;
import com.lc.qa.entity.User;
import com.lc.qa.service.QuestionService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
 * @since 2021-04-02
 */
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UcenterClient ucenterClient;

    @GetMapping("/{page}/{limit}")
    @ApiOperation("分页查询问题列表")
    public DiscreteResult pageQuestion(@PathVariable("page") Long page, @PathVariable("limit") Long limit) {
        Page<Question> questionPage = new Page<>(page, limit);
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        // 按照更新时间倒序查询
        questionService.page(questionPage, wrapper.orderByDesc("update_time"));
        return DiscreteResult.ok().data("total", questionPage.getTotal()).data("rows", questionPage.getRecords());
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id获取问题详情信息")
    public DiscreteResult getQuestion(@PathVariable("id") String id) {
        Question question = questionService.getById(id);
        if (question != null) {
            return DiscreteResult.ok().data("question", question);
        } else {
            return DiscreteResult.error().message("问题详情获取失败！");
        }
    }

    @GetMapping("/my-question/{page}/{limit}")
    @ApiOperation("我的提问")
    public DiscreteResult myQuestion(HttpServletRequest request,
                                     @PathVariable("page") Long page,
                                     @PathVariable("limit") Long limit) {
        String userId = JwtUtils.getUserIdByJwtToken(request);
        if (StringUtils.isEmpty(userId)) {
            return DiscreteResult.ok().status(ResultStatus.UNAUTHORIZED.getCode()).message("请先登录！");
        } else {
            Page<Question> questionPage = new Page<>(page, limit);
            QueryWrapper<Question> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            questionService.page(questionPage, wrapper);
            return DiscreteResult.ok().data("rows", questionPage.getRecords()).data("total", questionPage.getTotal());
        }
    }

    @PostMapping("/add")
    @ApiOperation("添加问题")
    public DiscreteResult addQuestion(@RequestBody Question question, HttpServletRequest request) {
        String userId = JwtUtils.getUserIdByJwtToken(request);
        if (StringUtils.isEmpty(userId)) {
            return DiscreteResult.ok().status(ResultStatus.UNAUTHORIZED.getCode()).message("请先登录！");
        } else {
            // 调用用户中心微服务获取提问人相关信息
            DiscreteResult result = ucenterClient.getUserInfo(userId);
            if (result.getSuccess()) {
                Map<String, Object> data = result.getData();
                Object userInfo = data.get("userInfo");
                ObjectMapper objectMapper = new ObjectMapper();
                User user = objectMapper.convertValue(userInfo, User.class);
                // 封装提问人的信息
                question.setAvatar(user.getAvatar());
                question.setUsername(user.getUsername());
                question.setUserId(userId);
                boolean success = questionService.save(question);
                return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器错误，提问失败！");
            } else {
                return DiscreteResult.error().message("获取提问人信息失败！");
            }
        }
    }
}

