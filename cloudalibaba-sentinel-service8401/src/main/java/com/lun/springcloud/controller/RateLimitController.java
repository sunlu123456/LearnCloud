package com.lun.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.lun.springcloud.entities.CommonResult;
import com.lun.springcloud.entities.Payment;
import com.lun.springcloud.myhandler.CustomerBlockHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @SentinelResource 注解
 * 注意：注解方式埋点不支持 private 方法。
 * blockHandler 函数访问范围需要是 public，返回类型需要与原方法相匹配，
 * 参数类型需要和原方法相匹配并且最后加一个额外的参数，类型为 BlockException
 */
@RestController
public class RateLimitController {
    /**
     * 经测试sentinel使用url:/byResource配置限流不走handleException方法，
     * 返回Sentinel自带的限流处理结果 Blocked by Sentinel (flow limiting)
     * 使用资源名：byResource配置限流规则走自己定义的兜底方法
     */
    @GetMapping("/byResource")
    @SentinelResource(value = "byResource",blockHandler = "handleException")
    public CommonResult byResource() {
        return new CommonResult(200,"按资源名称限流测试OK",new Payment(2020L,"serial001"));
    }

    public CommonResult handleException(BlockException exception) {
        return new CommonResult(444,exception.getClass().getCanonicalName()+"\t 服务不可用");
    }

    /**
     * 快速点击http://localhost:8401/rateLimit/byUrl
     * 结果 - 会返回Sentinel自带的限流处理结果 Blocked by Sentinel (flow limiting)
     * @return
     */
    @GetMapping("/rateLimit/byUrl")
    @SentinelResource(value = "byUrl")
    public CommonResult byUrl()
    {
        return new CommonResult(200,"按url限流测试OK",new Payment(2020L,"serial002"));
    }

    /**
     * 使用自定义的全局限流处理逻辑
     */
    @GetMapping("/rateLimit/customerBlockHandler")
    @SentinelResource(value = "customerBlockHandler",
            blockHandlerClass = CustomerBlockHandler.class,//<-------- 自定义限流处理类
            blockHandler = "handlerException2")//<-----------自定义限流处理方法
    public CommonResult customerBlockHandler()
    {
        return new CommonResult(200,"按客戶自定义",new Payment(2020L,"serial003"));
    }
}
