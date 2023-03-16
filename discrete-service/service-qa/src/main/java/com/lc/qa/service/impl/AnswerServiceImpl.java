package com.lc.qa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lc.qa.entity.Answer;
import com.lc.qa.entity.Question;
import com.lc.qa.entity.SonAnswer;
import com.lc.qa.entity.TreeAnswer;
import com.lc.qa.mapper.AnswerMapper;
import com.lc.qa.service.AnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lc
 * @since 2021-04-02
 */
@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements AnswerService {

    @Override
    public List<TreeAnswer> getTreeAnswer(Page<Answer> answerPage, String questionId) {
        QueryWrapper<Answer> wrapper = new QueryWrapper<>();
        wrapper.eq("question_id", questionId);
        wrapper.eq("parent_id", 0);
        wrapper.eq("reply_id", 0);
        baseMapper.selectPage(answerPage, wrapper);
        // 此时这个answerList只是所有回答的list（一级评论）
        List<Answer> answerList = answerPage.getRecords();
        // 我们还需要获取这个问题下的所有讨论来放入回答下
        QueryWrapper<Answer> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("question_id", questionId);
        // 这个问题下的所有list（一级二级三级都有）
        List<Answer> allAnswerList = baseMapper.selectList(wrapper1);
        ArrayList<TreeAnswer> treeAnswers = new ArrayList<>();
        // 先筛选回答，也就是parentId为0的
        for (Answer answer : answerList) {
            if ("0".equals(answer.getParentId())) {
                TreeAnswer treeAnswer = new TreeAnswer();
                // 回答相关
                treeAnswer.setId(answer.getId());
                treeAnswer.setContent(answer.getContent());
                // 用户相关
                treeAnswer.setUsername(answer.getUsername());
                treeAnswer.setUserId(answer.getUserId());
                treeAnswer.setAvatar(answer.getAvatar());
                // 时间
                treeAnswer.setCreateTime(answer.getCreateTime());
                // 子评论
                treeAnswer.setChildren(getChildren(allAnswerList, answer.getId()));
                // 放入结果集
                treeAnswers.add(treeAnswer);
            }
        }
        return treeAnswers;
    }

    @Override
    public IPage<Question> getMyAnswer(IPage<Question> questionIPage, String userId) {
        return baseMapper.getMyAnswer(questionIPage, userId);
    }

    /**
     * 根据传入的父id筛选answerList中对应子评论
     * @param answerList answerList
     * @param parentId parentId
     * @return answerList
     */
    private List<SonAnswer> getChildren(List<Answer> answerList, String parentId) {
        ArrayList<SonAnswer> sonAnswers = new ArrayList<>();
        for (Answer answer : answerList) {
            if (parentId.equals(answer.getParentId())) {
                // 找到对应子评论，开始赋值
                SonAnswer sonAnswer = new SonAnswer();
                // 评论相关
                sonAnswer.setId(answer.getId());
                sonAnswer.setContent(answer.getContent());
                sonAnswer.setCreateTime(answer.getCreateTime());
                // 用户相关
                sonAnswer.setUserId(answer.getUserId());
                sonAnswer.setAvatar(answer.getAvatar());
                sonAnswer.setUsername(answer.getUsername());
                // 是否是不纯正的子评论，就是是在这个回答下回复其他人的评论，就需要设置一下回复人名称
                if (!answer.getReplyId().equals(answer.getParentId())) {
                    for (Answer answerChild : answerList) {
                        if (answerChild.getId().equals(answer.getReplyId())) {
                            // 找到他到底是回复的哪一条评论，设置回复人名称
                            sonAnswer.setReplyName(answerChild.getUsername());
                        }
                    }
                }
                // 处理完成一条子评论，放入集合，结束
                sonAnswers.add(sonAnswer);
            }
        }
        return sonAnswers;
    }

}
