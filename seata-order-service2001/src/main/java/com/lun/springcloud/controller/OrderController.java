package com.lun.springcloud.controller;

import com.lun.springcloud.domain.CommonResult;
import com.lun.springcloud.domain.Order;
import com.lun.springcloud.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class OrderController {
    @Resource
    private OrderService orderService;

    /**
     * 测试 http://localhost:2001/order/create?userId=1&productId=1&count=10&money=100
     * @param order
     * @return
     */
    @GetMapping("/order/create")
    public CommonResult create(Order order)
    {
        orderService.create(order);
        return new CommonResult(200,"订单创建成功");
    }
}
