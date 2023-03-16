package com.lc.qa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lc.qa.entity.Answer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.qa.entity.Question;
import com.lc.qa.entity.TreeAnswer;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lc
 * @since 2021-04-02
 */
public interface AnswerService extends IService<Answer> {

    /**
     * 分页获取指定问题树形回答数据
     * @param answerPage 分页对象
     * @param questionId 问题id
     * @return 树形结构回答数据
     */
    List<TreeAnswer> getTreeAnswer(Page<Answer> answerPage, String questionId);

    /**
     * 分页获取指定员工回答过的问题
     * @param questionIPage 分页对象
     * @param userId 职工id
     * @return 分页对象
     */
    IPage<Question> getMyAnswer(IPage<Question> questionIPage, String userId);

}
