package com.lc.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lc.learning.entity.Teacher;
import com.lc.learning.entity.vo.TeacherQuery;
import com.lc.learning.mapper.TeacherMapper;
import com.lc.learning.service.TeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lc
 * @since 2021-02-21
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Override
    public void pageQuery(Page<Teacher> pageParam, TeacherQuery teacherQuery) {
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        if (teacherQuery != null) {
            String name = teacherQuery.getName();
            String begin = teacherQuery.getBegin();
            String end = teacherQuery.getEnd();
            if (StringUtils.isNotEmpty(name)) {
                wrapper.like("name", name);
            }
            if (StringUtils.isNotEmpty(begin)) {
                wrapper.ge("create_time", begin);
            }
            if (StringUtils.isNotEmpty(end)) {
                wrapper.le("create_time", end);
            }
        }
        baseMapper.selectPage(pageParam, wrapper);
    }
}
