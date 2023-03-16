package com.lc.ucenter.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：lc
 * @date ：Created in 2021/4/1 16:45
 * @modified By：
 */
@Data
public class AdminVo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "性别 1女，2男")
    private Integer gender;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "个人介绍")
    private String introduce;

    @ApiModelProperty(value = "用户身份")
    private Integer permission;

    @ApiModelProperty(value = "是否为管理员")
    private Integer isAdmin;
}
