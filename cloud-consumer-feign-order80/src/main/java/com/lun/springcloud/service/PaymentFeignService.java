package com.lun.springcloud.service;

import com.lun.springcloud.entities.CommonResult;
import com.lun.springcloud.entities.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * OpenFeign服务调用（可以作为微服务之间接口调用的API使用）
 * 接口+注解：微服务调用接口 + @FeignClient
 *
 * OpenFeign是Spring Cloud在Feign的基础上支持了SpringMVC的注解，如@RequesMapping等等。
 * OpenFeign的@Feignclient可以解析SpringMVc的@RequestMapping注解下的接口，
 * 并通过动态代理的方式产生实现类，实现类中做负载均衡并调用其他服务，并且集成了Ribbon。
 */
@Component
@FeignClient(value = "CLOUD-PAYMENT-SERVICE")//微服务在注册中心中的名称
public interface PaymentFeignService {

    @GetMapping(value = "/payment/get/{id}")//请求支付微服务中的获取订单明细接口
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id);

    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout();
}
