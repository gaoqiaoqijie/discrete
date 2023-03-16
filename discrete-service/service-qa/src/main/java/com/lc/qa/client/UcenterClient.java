package com.lc.qa.client;


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
     * 通过userfId来获取用户信息
     * @param userfId userfId
     * @return TrainingResult
     */
    @GetMapping("/info/{userfId}")
    DiscreteResult getUserInfo(@PathVariable(value = "userfId") String userfId);
}
