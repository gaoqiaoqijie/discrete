package com.lc.ucenter.controller;


import com.lc.common.utils.JwtUtils;
import com.lc.common.utils.DiscreteResult;
import com.lc.ucenter.entity.Admin;
import com.lc.ucenter.entity.vo.AdminVo;
import com.lc.ucenter.entity.vo.LoginVo;
import com.lc.ucenter.service.AdminService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lc
 * @since 2021-04-01
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    @ApiOperation("管理员后台登录")
    public DiscreteResult adminLogin(@RequestBody LoginVo loginVo) {
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return DiscreteResult.error();
        }
        String token = adminService.login(username, password);
        return DiscreteResult.ok().data("token", token);
    }

    @GetMapping("/info")
    @ApiOperation("管理员登录后获取信息（根据token）")
    public DiscreteResult getInfo(HttpServletRequest request) {
        String adminId = JwtUtils.getUserIdByJwtToken(request);
        Admin adminInfo = adminService.getInfo(adminId);
        return DiscreteResult.ok().data("avatar", adminInfo.getAvatar()).data("name", adminInfo.getUsername()).data("roles", "admin");
    }

    @PostMapping("/add")
    @ApiOperation("添加教师")
    public DiscreteResult addAdmin(@RequestBody AdminVo adminVo) {
        boolean success=false;
        // 判断是否有管理员权限
        if(adminVo.getIsAdmin()==1){
            success = adminService.addAdminTeacher(adminVo);
        }else {
            success = adminService.addTeacher(adminVo);
        }
        return success ? DiscreteResult.ok() : DiscreteResult.error().message("服务器错误，添加失败！");
    }

}

