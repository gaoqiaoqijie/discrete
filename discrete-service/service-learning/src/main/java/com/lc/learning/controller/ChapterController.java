package com.lc.learning.controller;


import com.lc.common.utils.DiscreteResult;
import com.lc.learning.entity.Chapter;
import com.lc.learning.entity.vo.TreeChapter;
import com.lc.learning.service.ChapterService;
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
 * @since 2021-02-25
 */
@RestController
@RequestMapping("/chapter")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @GetMapping("/treeList/{courseId}")
    @ApiOperation("获取课程下章节树形结构数据")
    public DiscreteResult getTreeList(@PathVariable(value = "courseId") String courseId) {
        List<TreeChapter> treeChapter = chapterService.getTreeChapter(courseId);
        return DiscreteResult.ok().data("treeChapter", treeChapter);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据章节id获取章节信息")
    public DiscreteResult getById(@PathVariable(value = "id") String chapterId) {
        Chapter chapter = chapterService.getById(chapterId);
        return DiscreteResult.ok().data("chapter", chapter);
    }

    @PostMapping("/add")
    @ApiOperation("添加章节")
    public DiscreteResult addChapter(@RequestBody Chapter chapter) {
        boolean success = chapterService.save(chapter);
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器错误，添加失败！");
    }

    @PostMapping("/update")
    @ApiOperation("更新章节")
    public DiscreteResult updateChapter(@RequestBody Chapter chapter) {
        boolean success = chapterService.saveOrUpdate(chapter);
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器错误，修改失败！");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除章节")
    public DiscreteResult deleteChapter(@PathVariable(value = "id") String id) {
        boolean success = chapterService.deleteChapter(id);
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器错误，删除失败！");
    }
}

