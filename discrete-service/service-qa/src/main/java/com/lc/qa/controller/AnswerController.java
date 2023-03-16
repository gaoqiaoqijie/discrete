package com.lc.qa.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.common.utils.JwtUtils;
import com.lc.common.utils.ResultStatus;
import com.lc.common.utils.DiscreteResult;
import com.lc.qa.client.UcenterClient;
import com.lc.qa.entity.Answer;
import com.lc.qa.entity.Question;
import com.lc.qa.entity.User;
import com.lc.qa.entity.TreeAnswer;
import com.lc.qa.service.AnswerService;
import com.lc.qa.service.QuestionService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
@RequestMapping("/answer")
public class AnswerController {

    @Autowired
    private UcenterClient ucenterClient;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;


    @GetMapping("/{page}/{limit}/{questionId}")
    @ApiOperation("分页获取指定问题下的回答及评论数据")
    public DiscreteResult getTreeAnswer(@PathVariable("page") Long page,
                                        @PathVariable("limit") Long limit,
                                        @PathVariable("questionId") String questionId) {
        Page<Answer> answerPage = new Page<>(page, limit);
        List<TreeAnswer> treeAnswer = answerService.getTreeAnswer(answerPage, questionId);
        return DiscreteResult.ok().data("treeAnswer", treeAnswer).data("total", answerPage.getTotal());
    }

    @GetMapping("/user/{page}/{limit}")
    public DiscreteResult getUserAnswer(@PathVariable("page") Long page,
                                        @PathVariable("limit") Long limit,
                                        String userId){
        Page<Question> questionPage = new Page<>(page, limit);
        answerService.getMyAnswer(questionPage, userId);
        return DiscreteResult.ok().data("total", questionPage.getTotal()).data("rows", questionPage.getRecords());
    }



    @PostMapping("/add")
    @ApiOperation("添加回答")
    public DiscreteResult addAnswer(@RequestBody Answer answer, HttpServletRequest request) {
        // 检查登录
        String userId = JwtUtils.getUserIdByJwtToken(request);
        if (StringUtils.isEmpty(userId)) {
            return DiscreteResult.ok().status(ResultStatus.UNAUTHORIZED.getCode()).message("请先登录！");
        } else {
            // 调用用户中心微服务获取回答人相关信息
            DiscreteResult result = ucenterClient.getUserInfo(userId);
            if (result.getSuccess()) {
                Map<String, Object> data = result.getData();
                Object userInfo = data.get("userInfo");
                ObjectMapper objectMapper = new ObjectMapper();
                User user = objectMapper.convertValue(userInfo, User.class);
                // 设置回答人相关信息
                answer.setAvatar(user.getAvatar());
                answer.setUsername(user.getUsername());
                answer.setUserId(userId);
                boolean success = answerService.save(answer);
                // 回答成功之后问题表回答数+1
                if (success) {
                    success = questionService.updateAnswerNum(answer.getQuestionId());
                }
                return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器错误！回答失败！");
            } else {
                return DiscreteResult.error().message("获取回答人信息失败！");
            }
        }
    }
}

