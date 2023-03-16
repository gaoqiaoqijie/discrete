package com.lc.common.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一结果返回封装
 * @author lc
 */
@Data
public class DiscreteResult {
    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String,Object> data = new HashMap<>();

    /**
     * 构造方法私有化，单例模式
     */
    private DiscreteResult(){};

    /**
     * 给外界提供获取对象的静态方法
     * @return TrainingResult
     */
    public static DiscreteResult ok(){
        DiscreteResult discreteResult = new DiscreteResult();
        discreteResult.setSuccess(true);
        discreteResult.setCode(ResultStatus.SUCCESS.getCode());
        discreteResult.setMessage("成功");
        return discreteResult;
    }

    public static DiscreteResult error(){
        DiscreteResult discreteResult = new DiscreteResult();
        discreteResult.setSuccess(false);
        discreteResult.setCode(ResultStatus.ERROR.getCode());
        discreteResult.setMessage("失败");
        return discreteResult;
    }

    /**
     * 以下设置是为了方便后面链式编程
     */
    public DiscreteResult success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public DiscreteResult message(String message){
        this.setMessage(message);
        return this;
    }

    public DiscreteResult status(Integer status){
        this.setCode(status);
        return this;
    }

    public DiscreteResult data(String key, Object value){
        this.data.put(key,value);
        return this;
    }

    public DiscreteResult data(Map<String,Object> map){
        this.setData(map);
        return this;
    }
}
