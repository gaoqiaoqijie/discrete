package com.lc.learning.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lc.common.utils.DiscreteResult;
import com.lc.learning.entity.Teacher;
import com.lc.learning.entity.vo.TeacherQuery;
import com.lc.learning.service.TeacherService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lc
 * @since 2021-02-21
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping("/{page}/{limit}")
    @ApiOperation("条件分页查询讲师数据")
    public DiscreteResult getPageTeacher(@PathVariable(value = "page") Long page,
                                         @PathVariable(value = "limit") Long limit,
                                         @RequestBody(required = false) TeacherQuery teacherQuery) {
        Page<Teacher> teacherPage = new Page<>(page, limit);
        teacherService.pageQuery(teacherPage, teacherQuery);
        long total = teacherPage.getTotal();
        List<Teacher> rows = teacherPage.getRecords();
        return DiscreteResult.ok().data("total", total).data("rows", rows);
    }

    @GetMapping("/all")
    @ApiOperation("获取所有讲师数据")
    public DiscreteResult getAllTeacher() {
        List<Teacher> teacherList = teacherService.list(null);
        return DiscreteResult.ok().data("teacherList", teacherList);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id获取讲师信息")
    public DiscreteResult getTeacherById(@PathVariable(value = "id") String id) {
        Teacher teacher = teacherService.getById(id);
        return DiscreteResult.ok().data("teacher", teacher);
    }

    @PostMapping("/add")
    @ApiOperation("添加讲师")
    public DiscreteResult addTeacher(@RequestBody Teacher teacher) {
        boolean success = teacherService.save(teacher);
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器异常，添加失败！");
    }

    @PostMapping("{id}")
    @ApiOperation("根据id更新讲师信息")
    public DiscreteResult updateTeacher(@RequestBody Teacher teacher) {
        boolean success = teacherService.updateById(teacher);
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器异常，修改失败！");
    }

    @DeleteMapping("{id}")
    @ApiOperation("根据id删除讲师")
    public DiscreteResult deleteTeacher(@PathVariable(value = "id") String id) {
        boolean success = teacherService.removeById(id);
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("删除失败！");
    }

}

