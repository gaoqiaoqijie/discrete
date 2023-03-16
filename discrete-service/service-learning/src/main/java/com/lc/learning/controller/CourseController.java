package com.lc.learning.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lc.common.utils.DiscreteResult;
import com.lc.learning.entity.Course;
import com.lc.learning.entity.vo.CoursePublishVo;
import com.lc.learning.entity.vo.CourseQuery;
import com.lc.learning.entity.vo.CourseVo;
import com.lc.learning.service.CourseService;
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
 * @since 2021-02-24
 */
@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;


    @GetMapping("{id}")
    @ApiOperation("根据id获取课程基本信息")
    public DiscreteResult getCourseInfo(@PathVariable(value = "id") String id) {
        Course course = courseService.getById(id);
        return DiscreteResult.ok().data("course", course);
    }

    @GetMapping("/all")
    @ApiOperation("获取所有课程数据")
    public DiscreteResult getAllCourse() {
        // 默认查询已发布的
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.eq("status","Normal");
        List<Course> list = courseService.list(wrapper);
        return DiscreteResult.ok().data("courseList", list);
    }

    @PostMapping("/{page}/{limit}")
    @ApiOperation("条件分页查询课程数据")
    public DiscreteResult pageCourse(@PathVariable(value = "page") Long page,
                                     @PathVariable(value = "limit") Long limit,
                                     @RequestBody(required = false) CourseQuery courseQuery) {
        Page<Course> coursePage = new Page<>(page, limit);
        courseService.pageQuery(coursePage, courseQuery);
        return DiscreteResult.ok().data("rows", coursePage.getRecords()).data("total", coursePage.getTotal());
    }

    @GetMapping("/publish/info/{id}")
    @ApiOperation("根据课程id获取课程发布信息")
    public DiscreteResult getCourseInfoPublish(@PathVariable(value = "id") String id) {
        CoursePublishVo coursePublishInfo = courseService.getCoursePublishInfo(id);
        return DiscreteResult.ok().data("coursePublish", coursePublishInfo);
    }

    @PostMapping("/add")
    @ApiOperation("添加课程基本信息")
    public DiscreteResult addCourse(@RequestBody CourseVo courseVo) {
        String courseId = courseService.addCourse(courseVo);
        return DiscreteResult.ok().data("courseId", courseId);
    }

    @GetMapping("/publish/{id}")
    @ApiOperation("发布课程")
    public DiscreteResult publishCourse(@PathVariable(value = "id") String id) {
        boolean success = courseService.publishCourse(id);
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器错误！发布失败！");
    }

    @GetMapping("/publish/save/{id}")
    @ApiOperation("暂存待发布课程")
    public DiscreteResult savePublishCourse(@PathVariable(value = "id") String id) {
        boolean success = courseService.savePublishCourse(id);
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器错误！暂存失败！");
    }

    @PostMapping("/update")
    @ApiOperation("更新课程基本信息")
    public DiscreteResult updateCourse(@RequestBody CourseVo courseVo) {
        boolean success = courseService.updateCourse(courseVo);
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器错误！更新失败！");
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除课程信息")
    public DiscreteResult deleteCourse(@PathVariable(value = "id") String id) {
        boolean success = courseService.deleteCourse(id);
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器错误！课程删除失败！");

    }
}

