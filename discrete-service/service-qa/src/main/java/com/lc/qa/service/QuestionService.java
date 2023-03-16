package com.lc.qa.service;

import com.lc.qa.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lc
 * @since 2021-04-02
 */
public interface QuestionService extends IService<Question> {

    /**
     * 根据问题id更新回答数量（默认+1）
     * @param questionId 问题id
     * @return 是否成功
     */
    boolean updateAnswerNum(String questionId);

}
