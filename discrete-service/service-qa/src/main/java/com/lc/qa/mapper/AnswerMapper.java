package com.lc.qa.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lc.qa.entity.Answer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lc.qa.entity.Question;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author houcheng
 * @since 2021-04-02
 */
public interface AnswerMapper extends BaseMapper<Answer> {


    /**
     * 获取指定职工回答过的问题
     * @param questionIPage 分页对象
     * @param userId 职工id
     * @return 分页对象
     */
    IPage<Question> getMyAnswer(IPage<Question> questionIPage, String userId);

}
