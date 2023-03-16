package com.lc.ucenter.service;

import com.lc.ucenter.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lc.ucenter.entity.vo.AdminVo;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lc
 * @since 2021-04-01
 */
public interface AdminService extends IService<Admin> {

    /**
     * 登录处理
     * @param username 昵称
     * @param password 密码
     * @return 成功返回token
     */
    String login(HttpServletRequest servletRequest,String username, String password);
    String login(String username, String password);

    /**
     * 根据id获取信息
     * @param adminId 管理员id
     * @return 管理员对象
     */
    Admin getInfo(String adminId);

    /**
     * 添加管理员教师
     * @param adminVo adminVo
     * @return 是否成功
     */
    @Transactional
    boolean addAdminTeacher(AdminVo adminVo);

    boolean addTeacher(AdminVo adminVo);
}
