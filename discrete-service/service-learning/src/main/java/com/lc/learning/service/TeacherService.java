package com.lc.learning.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lc.learning.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.learning.entity.vo.TeacherQuery;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lc
 * @since 2021-02-21
 */
public interface TeacherService extends IService<Teacher> {

    /**
     * 条件分页查询
     * @param pageParam 分页对象
     * @param teacherQuery 讲师查询vo
     */
    void pageQuery(Page<Teacher> pageParam, TeacherQuery teacherQuery);
}
