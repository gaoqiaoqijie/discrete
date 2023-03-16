package com.lc.learning.controller;

import com.lc.common.utils.DiscreteResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：lc
 * @date ：Created in 2021/2/21 21:30
 * @modified By：
 */
@RestController
public class LoginController {

    @Value("${default-setting.avatar}")
    private String avatar;

    @PostMapping("/user/login")
    @ApiOperation("临时处理后台登录")
    public DiscreteResult login() {
        return DiscreteResult.ok().data("token","admin");
    }

    @GetMapping("/user/info")
    @ApiOperation("临时处理获取信息")
    public DiscreteResult info() {
        return DiscreteResult.ok()
                .data("roles", "[admin]")
                .data("name", "admin")
                .data("avatar", avatar);
    }

}
