package com.lun.springcloud.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.lun.springcloud.entities.CommonResult;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentService {

    //测试链路流控
    @SentinelResource(value = "getOrder",blockHandler = "handleException")       // 将此方法标注为sentinel的资源。value=资源名
    public CommonResult getOrder()
    {
        return new CommonResult(0, String.valueOf(new Random().nextInt()));
    }

    public CommonResult handleException(BlockException ex) {
        return new CommonResult(-1,
                ex.getClass().getCanonicalName() + "\t服务不可用");
    }
}
