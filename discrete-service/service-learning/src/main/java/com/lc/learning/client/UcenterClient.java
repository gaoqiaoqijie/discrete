package com.lc.learning.client;


import com.lc.common.utils.DiscreteResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用户中心微服务远程调用
 * @author lc
 */
@FeignClient(name = "service-ucenter")
@Component
public interface UcenterClient {


    /**
     * 通过StaffId来获取用户信息
     * @param staffId staffId
     * @return TrainingResult
     */
    @GetMapping("/info/{userId}")
    DiscreteResult getUserInfo(@PathVariable(value = "userId") String staffId);
}
