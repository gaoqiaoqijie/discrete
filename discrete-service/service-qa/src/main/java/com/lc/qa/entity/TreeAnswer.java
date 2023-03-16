package com.lc.qa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ：lc
 * @date ：Created in 2021/4/2 21:05
 * @modified By：
 */
@Data
public class TreeAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "回答内容")
    private String content;

    @ApiModelProperty(value = "回答人id")
    private String userId;

    @ApiModelProperty(value = "回答人头像")
    private String avatar;

    @ApiModelProperty(value = "回答人用户名")
    private String username;

    @ApiModelProperty(value = "子评论List")
    private List<SonAnswer> children;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
