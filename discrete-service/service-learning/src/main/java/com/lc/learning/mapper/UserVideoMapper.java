package com.lc.learning.mapper;

import com.lc.learning.entity.UserVideo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lc
 * @since 2021-03-11
 */
public interface UserVideoMapper extends BaseMapper<UserVideo> {

    /**
     * 获取指定员工的学习时长
     * @param userId 员工id
     * @return Integer 的学习时长
     */
    Integer getLearning(String userId);

}
