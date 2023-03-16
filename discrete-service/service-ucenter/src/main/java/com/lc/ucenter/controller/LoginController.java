package com.lc.ucenter.controller;


import com.lc.common.utils.JwtUtils;
import com.lc.common.utils.ResultStatus;
import com.lc.common.utils.DiscreteResult;
import com.lc.ucenter.entity.User;
import com.lc.ucenter.entity.vo.LoginVo;
import com.lc.ucenter.entity.vo.RegisterVo;
import com.lc.ucenter.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lc
 * @since 2021-03-05
 */
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("send/{mail}")
    @ApiOperation("往指定邮箱发送验证码")
    public DiscreteResult sendCode(@PathVariable(value = "mail") String mail) {
        // 先判空
        if (StringUtils.isEmpty(mail)) {
            return DiscreteResult.error().message("邮箱不能为空");
        }
        // 查看是否获取过验证码了
        String code = redisTemplate.opsForValue().get(mail);
        if (StringUtils.isNotEmpty(code)) {
            return DiscreteResult.error().message("已经发送过验证码了，请稍后获取");
        }
        // 发送验证码
        code = userService.sendCode(mail);
        // 如果发送成功的话，就存入redis，并且设置过期时间五分钟
        redisTemplate.opsForValue().set(mail, code, 5, TimeUnit.MINUTES);
        // 返回成功信息
        return DiscreteResult.ok();
    }

    @PostMapping("register")
    @ApiOperation("用户注册")
    public DiscreteResult register(@RequestBody RegisterVo member) {
        if (member == null) {
            return DiscreteResult.error().message("请输入数据！");
        }
        boolean result = userService.register(member);
        if (result) {
            // 注册成功后自动登录
            Map<String, Object> map = userService.login(member.getUsername(), member.getPassword());
            return DiscreteResult.ok().message("注册成功！").data(map);
        }
        return DiscreteResult.error();
    }

    @PostMapping("login")
    @ApiOperation("用户登录")
    public DiscreteResult login(@RequestBody LoginVo loginVo) {
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return DiscreteResult.error().message("用户名或密码为空！");
        }
        Map<String, Object> map = userService.login(username, password);
        return DiscreteResult.ok().data(map);
    }

    @GetMapping("info")
    @ApiOperation("根据请求头中的token信息获取用户信息")
    public DiscreteResult getInfo(HttpServletRequest request) {
        String userId = JwtUtils.getUserIdByJwtToken(request);
        if (StringUtils.isEmpty(userId)) {
            return DiscreteResult.error().status(ResultStatus.UNAUTHORIZED.getCode()).message("请先登录！");
        }
        User user = userService.getInfo(userId);
        user.setPassword(null);
        if (user.getPermission() == 2) {
            return DiscreteResult.ok().data("avatar", user.getAvatar()).data("name", user.getUsername()).data("roles", "admin");
        } else {
            return DiscreteResult.ok().data("userInfo", user);
        }
    }

    @GetMapping("info/{userId}")
    @ApiOperation("根据用户id获取用户信息")
    public DiscreteResult getUserInfo(@PathVariable(value = "userId") String userId) {
        User info = userService.getInfo(userId);
        // 返回给前台时清空密码等一些不必要的信息
        info.setPassword(null);
        return DiscreteResult.ok().data("userInfo", info);
    }
}

