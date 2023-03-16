package com.lc.ucenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lc.common.utils.ResultStatus;
import com.lc.common.utils.DiscreteResult;
import com.lc.ucenter.entity.User;
import com.lc.ucenter.entity.vo.PasswordVo;
import com.lc.ucenter.entity.vo.UserQuery;
import com.lc.ucenter.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ：lc
 * @date ：Created in 2021/3/18 20:21
 * @modified By：
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    @ApiOperation("获取所有用户信息")
    public DiscreteResult getAllUser() {
        List<User> list = userService.list(null);
        return DiscreteResult.ok().data("userList", list);
    }

    @GetMapping("/all/teacher")
    @ApiOperation("获取所有用户信息")
    public DiscreteResult getAllTeacher() {
        List<User> list = userService.getAllTeacher();
        return DiscreteResult.ok().data("userList", list);
    }

    @PostMapping("/info")
    @ApiOperation("更新用户信息")
    public DiscreteResult updateUserInfo(@RequestBody User user) {
        boolean success = userService.updateById(user);
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器错误，修改资料失败！");
    }

    @PostMapping("/pwd")
    @ApiOperation("更新密码信息")
    public DiscreteResult updatePassword(@RequestBody PasswordVo passwordVo) {
        // 参数检查
        if (StringUtils.isEmpty(passwordVo.getId())) {
            return DiscreteResult.error().status(ResultStatus.UNAUTHORIZED.getCode()).message("请先登录！");
        }
        if (StringUtils.isEmpty(passwordVo.getOldPassword())) {
            return DiscreteResult.error().message("请输入原密码！");
        }
        if (StringUtils.isEmpty(passwordVo.getNewPassword())) {
            return DiscreteResult.error().message("请输入新密码！");
        }
        // 更新
        boolean success = userService.updatePassword(passwordVo);
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器错误，密码修改失败！");
    }

    @PostMapping("/{page}/{limit}")
    @ApiOperation("条件分页查询用户数据")
    public DiscreteResult getPageUser(@PathVariable(value = "page") Long page,
                                      @PathVariable(value = "limit") Long limit,
                                      @RequestBody(required = false) UserQuery userQuery) {
        Page<User> userPage = new Page<>(page, limit);
        userService.pageQuery(userPage, userQuery);
        long total = userPage.getTotal();
        List<User> rows = userPage.getRecords();
        return DiscreteResult.ok().data("total", total).data("rows", rows);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id获取用户信息")
    public DiscreteResult getUserById(@PathVariable(value = "id") String id) {
        User user = userService.getById(id);
        return DiscreteResult.ok().data("user", user);
    }

    @DeleteMapping("{id}")
    @ApiOperation("根据id删除用户")
    public DiscreteResult deleteUser(@PathVariable(value = "id") String id) {
        boolean success = userService.removeById(id);
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("删除失败！");
    }

    @PutMapping("/resetpwd/{id}")
    @ApiOperation("重置密码")
    public DiscreteResult resetpwd(@PathVariable(value = "id") String id) {
        boolean success = userService.resetPwd(id);
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("重置失败！");
    }
}
