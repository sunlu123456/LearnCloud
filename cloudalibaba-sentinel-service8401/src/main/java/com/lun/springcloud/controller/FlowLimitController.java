package com.lun.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.lun.springcloud.entities.CommonResult;
import com.lun.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class FlowLimitController {

    @Resource
    private PaymentService paymentService;

    /***===============服务限流===============***/
    @GetMapping("/testA")
    public String testA() {
        //测试线程数限流使用
        try {
            TimeUnit.MILLISECONDS.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info(Thread.currentThread().getName() + "\t" + "...testA");
        return "------testA";
    }

    @GetMapping("/testB")
    public CommonResult testB() {
        log.info(Thread.currentThread().getName() + "\t" + "...testB");
        return paymentService.getOrder();
//        return "------testB";
    }

    /**
     * 测试链路流控======>/testC设置为入口资源，与/testB比较测试
     * 链路流控模式指的是，当从某个接口过来的资源达到限流条件时，开启限流。
     * 它的功能有点类似于针对来源配置项，区别在于：针对来源是针对上级微服务，
     * 而链路流控是针对上级接口，也就是说它的粒度更细。
     *
     * @return
     */
    @GetMapping("/testC")
    public CommonResult testC() {
        log.info(Thread.currentThread().getName() + "\t" + "...testC");
        return paymentService.getOrder();
//        return "------testC";
    }

    /**
     * WarmUp(预热限流)配置
     * 案例，阀值为10+预热时长设置5秒。
     * 系统初始化的阀值为10/ 3约等于3,即阀值刚开始为3;然后过了5秒后阀值才慢慢升高恢复到10
     * 默认coldFactor为3，即请求QPS 从 threshold / 3开始，经预热时长逐渐升至设定的QPS阈值。
     */
    @GetMapping("/testD")
    public String testD() {
        log.info(Thread.currentThread().getName() + "\t" + "...testD");
        return "------testD";
    }
    /**===============服务熔断降级===============**/
    /**
     * RT 平均响应时间
     * 平均响应时间(DEGRADE_GRADE_RT)：当1s内持续进入5个请求，对应时刻的平均响应时间（秒级）均超过阈值（ count，以ms为单位），
     * 那么在接下的时间窗口（DegradeRule中的timeWindow，以s为单位）之内，对这个方法的调用都会自动地熔断(抛出DegradeException )。
     *
     * 注意Sentinel 默认统计的RT上限是4900 ms，超出此阈值的都会算作4900ms，若需要变更此上限可以通过启动配置项-Dcsp.sentinel.statistic.max.rt=xxx来配置。
     *
     * 注意：Sentinel 1.7.0才有平均响应时间（DEGRADE_GRADE_RT），Sentinel 1.8.0的没有这项，
     * 取而代之的是慢调用比例 (SLOW_REQUEST_RATIO)。
     * 慢调用比例 (SLOW_REQUEST_RATIO)：选择以慢调用比例作为阈值，需要设置允许的慢调用 RT（即最大的响应时间），
     * 请求的响应时间大于该值则统计为慢调用。当单位统计时长（statIntervalMs）内请求数目大于设置的最小请求数目，
     * 并且慢调用的比例大于阈值，则接下来的熔断时长内请求会自动被熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），
     * 若接下来的一个请求响应时间小于设置的慢调用 RT 则结束熔断，若大于设置的慢调用 RT 则会再次被熔断。
     */
    @GetMapping("/testE")
    public String testE() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("testE 测试RT");
        return "------testE";
    }

    /**
     * 异常比例(DEGRADE_GRADE_EXCEPTION_RATIO)：当资源的每秒请求量 >= 5，
     * 并且每秒异常总数占通过量的比值超过阈值（ DegradeRule中的 count）之后，资源进入降级状态，
     * 即在接下的时间窗口( DegradeRule中的timeWindow，以s为单位）之内，对这个方法的调用都会自动地返回。异常比率的阈值范围是[0.0, 1.0]，代表0% -100%。
     *
     * 注意，与Sentinel 1.8.0相比，有些不同（Sentinel 1.8.0才有的半开状态），Sentinel 1.8.0的如下：
     *
     * 异常比例 (ERROR_RATIO)：当单位统计时长（statIntervalMs）内请求数目大于设置的最小请求数目，
     * 并且异常的比例大于阈值，则接下来的熔断时长内请求会自动被熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），
     * 若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。异常比率的阈值范围是 [0.0, 1.0]，代表 0% - 100%。
     */
    @GetMapping("/testF")
    public String testF() {
        log.info("testF 异常比例");
        int age = 10/0;
        return "------testF";
    }

    /**
     * 异常数( DEGRADE_GRADF_EXCEPTION_COUNT )：当资源近1分钟的异常数目超过阈值之后会进行熔断。
     * 注意由于统计时间窗口是分钟级别的，若timeWindow小于60s，则结束熔断状态后码可能再进入熔断状态。
     *
     * 注意，与Sentinel 1.8.0相比，有些不同（Sentinel 1.8.0才有的半开状态），Sentinel 1.8.0的如下：
     *
     * 异常数 (ERROR_COUNT)：当单位统计时长内的异常数目超过阈值之后会自动进行熔断。
     * 经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，
     * 否则会再次被熔断。
     */
    @GetMapping("/testG")
    public String testG()
    {
        log.info("testG 测试异常数");
        int age = 10/0;
        return "------testG 测试异常数";
    }

    /**========热点限流==========**/
    /**
     * 何为热点？热点即经常访问的数据。很多时候我们希望统计某个热点数据中访问频次最高的 Top K 数据，并对其访问进行限制。比如：
     *
     * 商品 ID 为参数，统计一段时间内最常购买的商品 ID 并进行限制
     * 用户 ID 为参数，针对一段时间内频繁访问的用户 ID 进行限制
     * 热点参数限流会统计传入参数中的热点参数，并根据配置的限流阈值与模式，对包含热点参数的资源调用进行限流。
     * 热点参数限流可以看做是一种特殊的流量控制，仅对包含热点参数的资源调用生效。
     * @param p1
     * @param p2
     * @return
     */
    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey",blockHandler/*兜底方法*/ = "deal_testHotKey")
    public String testHotKey(@RequestParam(value = "p1",required = false) String p1,
                             @RequestParam(value = "p2",required = false) String p2) {
        int age = 10/0;//@SentinelResource主管配置出错，运行出错该走异常走异常。经测试Sentinel 1.8.0以后走兜底方法。
        return "------testHotKey";
    }

    /*兜底方法*/
    public String deal_testHotKey (String p1, String p2, BlockException exception) {
        return "------deal_testHotKey,o(╥﹏╥)o";  //sentinel系统默认的提示：Blocked by Sentinel (flow limiting)
    }
}
