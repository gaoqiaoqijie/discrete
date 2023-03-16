package com.lc.ucenter.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 讲师查询条件封装
 * @author lc
 */
@Data
public class UserQuery {

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "起始时间")
    private String begin;

    @ApiModelProperty(value = "结束时间")
    private String end;
}
