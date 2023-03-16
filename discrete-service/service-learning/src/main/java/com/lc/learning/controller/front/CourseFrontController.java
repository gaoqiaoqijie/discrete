package com.lc.learning.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lc.common.utils.JwtUtils;
import com.lc.common.utils.ResultStatus;
import com.lc.common.utils.DiscreteResult;
import com.lc.learning.entity.Course;
import com.lc.learning.entity.vo.CourseQuery;
import com.lc.learning.entity.vo.TreeChapter;
import com.lc.learning.entity.vo.front.CourseInfoFrontVo;
import com.lc.learning.service.ChapterService;
import com.lc.learning.service.CourseService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author ：lc
 * @date ：Created in 2021/3/4 19:43
 */
@RestController
@RequestMapping("/front/course")
public class CourseFrontController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ChapterService chapterService;

    @PostMapping("/{page}/{limit}")
    @ApiOperation("条件分页查询课程数据")
    public DiscreteResult getAllCourse(@PathVariable(value = "page") Integer page,
                                       @PathVariable(value = "limit") Integer limit,
                                       @RequestBody(required = false) CourseQuery courseQuery) {
        Page<Course> coursePage = new Page<>(page, limit);
        Map<String, Object> map = courseService.courseFrontQuery(coursePage, courseQuery);
        return DiscreteResult.ok().data(map);
    }

    @GetMapping("/hot")
    @ApiOperation("获取热门课程，前四个，参考字段学习人数，浏览量")
    public DiscreteResult getHotCourse() {
        return DiscreteResult.ok().data("rows", courseService.getHotCourses());
    }

    @GetMapping("{id}")
    @ApiOperation("获取课程详情信息")
    public DiscreteResult getCourseInfo(@PathVariable(value = "id") String id, HttpServletRequest request) {
        try {
            // 检查是否登录，有可能token会过期
            String staffId = JwtUtils.getUserIdByJwtToken(request);
            if (null == staffId || staffId.length() == 0) {
                return DiscreteResult.ok().status(ResultStatus.UNAUTHORIZED.getCode()).message("请先登录！");
            }
            CourseInfoFrontVo frontVo = courseService.getCourseInfoFrontVoById(id);
            // 还要封装章节信息
            List<TreeChapter> treeChapter = chapterService.getTreeChapter(staffId, id);
            return DiscreteResult.ok().data("courseInfo", frontVo).data("chapterInfo", treeChapter);
        } catch (ExpiredJwtException e) {
            return DiscreteResult.ok().status(ResultStatus.UNAUTHORIZED.getCode()).message("身份验证过期，请重新登录！");
        }


    }

    @GetMapping("/join/{courseId}")
    @ApiOperation("参与课程")
    public DiscreteResult joinCourse(HttpServletRequest request, @PathVariable(value = "courseId") String courseId) {
        try {
            // 检查是否登录，有可能token会过期
            String staffId = JwtUtils.getUserIdByJwtToken(request);
            if (null == staffId || staffId.length() == 0) {
                return DiscreteResult.ok().status(ResultStatus.UNAUTHORIZED.getCode()).message("请先登录！");
            }
            // 已登录就正常添加记录
            boolean success = courseService.joinCourse(staffId, courseId);
            return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器错误，参与课程失败！");
        } catch (ExpiredJwtException e) {
            return DiscreteResult.ok().status(ResultStatus.UNAUTHORIZED.getCode()).message("身份验证过期，请重新登录！");
        }
    }
}
