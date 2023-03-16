package com.lc.ucenter.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：lc
 * @date ：Created in 2021/3/6 15:37
 * @modified By：
 */
@Data
public class LoginVo {
    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;
}
