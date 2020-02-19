package com.mochen.controller;

import com.mochen.entities.Dept;
import com.mochen.service.DeptClientService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;

@RestController
/*@Retry(name = "retryBackendA")*/ //aop方式测试重试
/*@CircuitBreaker(name = "backendA")*/
public class DeptController_Consumer {
    @Autowired
    private DeptClientService service;

    @RequestMapping(value = "/consumer/dept/get/{id}")
    public Dept get(@PathVariable("id") Long id) {
        return this.service.get(id);
    }


    /**
     * 测试重试
     *
     * @return
     */
    @RequestMapping(value = "/consumer/dept/testRetry/{id}")
    public Dept testRetry(@PathVariable("id") Long id) {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(5000))
                .build();
        Retry retry = Retry.of("id", config);
        Try<Dept> result = Try.ofSupplier(Retry.decorateSupplier(
                retry, () -> service.get(id)
        ));
        return result.get();
    }


    CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
            .failureRateThreshold(50)
            .waitDurationInOpenState(Duration.ofMillis(1000))
            .ringBufferSizeInHalfOpenState(20)
            .ringBufferSizeInClosedState(20)
            .build();

    /**
     * 测试断路器
     * <p>
     * 配置在yml中的话没法进行容错降级，所以我们采用编程式
     */
    @RequestMapping(value = "/consumer/dept/testCircuitBreaker/{id}")
    public Dept testCircuitBreaker(@PathVariable("id") Long id) {
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("backendA");
        Try<Dept> supplier = Try.ofSupplier(io.github.resilience4j.circuitbreaker.CircuitBreaker
                .decorateSupplier(circuitBreaker,
                        () -> service.get(id))).recover(e -> {//服务调用失败后，降级，可以返回空的，也可以从缓存中取，取决于业务
            Dept dept = new Dept();
            dept.setDname("服务降级部门");
            dept.setDb_source("缓存中取的数据");
            dept.setDeptno(0l);
            return dept;
        });
        //如果不想返回空数组，可以在这里返回对应的错误提示
//        if (result.isSuccess()){
//            return result.get();
//        }else {//发生异常后的处理
//            return "服务提供者异常";
//        }
        return supplier.get();
    }

    RateLimiterConfig config = RateLimiterConfig.custom()
            .limitRefreshPeriod(Duration.ofMillis(5000))
            .limitForPeriod(1)
            .timeoutDuration(Duration.ofMillis(500))
            .build();

   /* @RequestMapping(value = "/consumer/dept/testRateLimiter/{id}")
    public List<Dept> testRateLimiter(@PathVariable("id") Long id) {
        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("backendB", config);
        Supplier<List<Dept>> supplier = RateLimiter.decorateSupplier(rateLimiter, () ->
                service.list()
        );

        Try<List<Dept>> result = Try.ofSupplier(supplier).recover(e -> {
            List<Dept> deptList = new ArrayList<>();
            Dept dept = new Dept();
            dept.setDname("已到达限流");
            deptList.add(dept);
            return deptList;
        });
        return result.get();
    }*/


    /**
     *
     * @测试限流
     * @return
     */
    @RequestMapping(value = "/consumer/dept/testRateLimiter2/{id}")
    @RateLimiter(name = "backendB")
    public Dept testRateLimiter2(@PathVariable("id") Long id) {
        Dept dept = null;
        try {
            dept = service.get(id);
        } catch (RequestNotPermitted e) {
            dept = new Dept();
            dept.setDname("已达到最高限流次数");
            return dept;
        }
        return dept;
    }


    @RequestMapping(value = "/consumer/dept/list")
    public List<Dept> list() {
        return this.service.list();
    }

    @RequestMapping(value = "/consumer/dept/add")
    public Object add(Dept dept) {
        return this.service.add(dept);
    }
}
