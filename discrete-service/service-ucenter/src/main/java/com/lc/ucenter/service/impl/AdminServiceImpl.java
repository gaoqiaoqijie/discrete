package com.lc.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lc.common.exception.TrainingException;
import com.lc.common.utils.JwtUtils;
import com.lc.ucenter.entity.Admin;
import com.lc.ucenter.entity.User;
import com.lc.ucenter.entity.vo.AdminVo;
import com.lc.ucenter.mapper.AdminMapper;
import com.lc.ucenter.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lc.ucenter.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lc
 * @since 2021-04-01
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {


    @Value("${default-setting.passwd}")
    private String passwd;

    @Autowired
    private UserService userService;

    @Override
    public String login(HttpServletRequest servletRequest,String username, String password) {
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        Admin admin = baseMapper.selectOne(wrapper);
        // 是否可以成功查询到数据
        if (null == admin) {
            throw new TrainingException(500, "用户名或密码错误！");
        }
        // 校验密码
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(admin.getPassword())) {
            throw new TrainingException(500, "用户名或密码错误！");
        }
        // 通过重重检测，可以给token了
        return JwtUtils.getJwtToken(admin.getId(), admin.getUsername());
//        servletRequest.setAttribute("adminId",admin.getId());
//        return "login ok";
    }

    @Override
    public String login(String username, String password) {
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        Admin admin = baseMapper.selectOne(wrapper);
        // 是否可以成功查询到数据
        if (null == admin) {
            throw new TrainingException(500, "用户名或密码错误！");
        }
        // 校验密码
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(admin.getPassword())) {
            throw new TrainingException(500, "用户名或密码错误！");
        }
        // 通过重重检测，可以给token了
        return JwtUtils.getJwtToken(admin.getId(), admin.getUsername());

//        return "login ok";
    }

    @Override
    public Admin getInfo(String adminId) {
        return baseMapper.selectById(adminId);
    }

    @Override
    public boolean addAdminTeacher(AdminVo adminVo) {
        //添加管理员账号
        Admin admin = new Admin();
        admin.setAvatar(adminVo.getAvatar());
        admin.setUsername(adminVo.getUsername());
        // 密码加密
        admin.setPassword(DigestUtils.md5DigestAsHex(passwd.getBytes()));
        int insert = baseMapper.insert(admin);
        //添加普通账号
        boolean success = addTeacher(adminVo);
        return insert > 0 && success;
    }

    @Override
    public boolean addTeacher(AdminVo adminVo) {
        User user = new User();
        BeanUtils.copyProperties(adminVo,user);
        user.setPassword(DigestUtils.md5DigestAsHex(passwd.getBytes()));
        boolean save = userService.save(user);
        return save;
    }
}
