package com.lc.learning.client;

import com.lc.common.utils.DiscreteResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 视频微服务远程调用
 * @author lc
 */
@FeignClient(name = "service-video")
@Component
public interface VodClient {

    /**
     * 根据videoSourceId获取云端视频信息
     * @param videoSourceId videoSourceId
     * @return TrainingResult
     */
    @GetMapping("/vod/{videoSourceId}")
    DiscreteResult getVideoInfo(@PathVariable(value = "videoSourceId") String videoSourceId);

    /**
     * 根据videoSourceId获取视频播放凭证
     * @param id videoSourceId
     * @return TrainingResult
     */
    @GetMapping("/vod/playAuth/{videoSourceId}")
    DiscreteResult getPlayAuthById(@PathVariable("videoSourceId") String id);

    /**
     * 根据videoSourceId删除云端相应视频
     * @param videoSourceId videoSourceId
     * @return TrainingResult
     */
    @DeleteMapping("/vod/{videoSourceId}")
    DiscreteResult deleteVideo(@PathVariable(value = "videoSourceId") String videoSourceId);

}
