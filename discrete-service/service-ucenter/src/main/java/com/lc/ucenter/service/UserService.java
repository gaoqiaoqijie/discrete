package com.lc.ucenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lc.ucenter.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.ucenter.entity.vo.PasswordVo;
import com.lc.ucenter.entity.vo.RegisterVo;
import com.lc.ucenter.entity.vo.UserQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lc
 * @since 2021-03-05
 */
public interface UserService extends IService<User> {

    /**
     * 向目标邮箱发送验证码
     * @param mail 目标邮箱
     * @return 成功返回验证码，不成功抛出异常
     */
    String sendCode(String mail);

    /**
     * 注册方法
     * @param registerVo 前端页面封装会员对象
     * @return 是否成功
     */
    boolean register(RegisterVo registerVo);

    /**
     * 登录处理
     * @param username 昵称
     * @param password 密码
     * @return 成功返回token
     */
    Map<String, Object> login(String username, String password);

    /**
     * 根据用户id获取用户信息
     * @param userId 用户id
     * @return 用户信息，user
     */
    User getInfo(String userId);

    /**
     * 更新密码
     * @param passwordVo passwordVo
     * @return 是否成功
     */
    boolean updatePassword(PasswordVo passwordVo);

    /**
     * 获取用户分页信息
     * @param userPage page
     * @param userQuery 查询条件
     */
    void pageQuery(Page<User> userPage, UserQuery userQuery);

    /**
     * 重置密码
     * @param id
     * @return 是否成功
     */
    boolean resetPwd(String id);

    boolean ebableById(String id);

    List<User> getAllTeacher();
}
