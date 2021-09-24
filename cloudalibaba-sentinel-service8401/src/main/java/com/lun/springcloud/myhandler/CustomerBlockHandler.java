package com.lun.springcloud.myhandler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.lun.springcloud.entities.CommonResult;
/**
 *自定义全局统—的限流处理逻辑，实现与业务代码解耦
 * 注意：方法要使用public static
 */
public class CustomerBlockHandler {

    public static CommonResult handlerException(BlockException exception) {
        return new CommonResult(4444,"按客戶自定义,global handlerException----1");
    }

    public static CommonResult handlerException2(BlockException exception) {
        return new CommonResult(4444,"按客戶自定义,global handlerException----2");
    }
}
