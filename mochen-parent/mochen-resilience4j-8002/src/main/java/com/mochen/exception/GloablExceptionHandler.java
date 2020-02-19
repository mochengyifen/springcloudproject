package com.mochen.exception;

import com.mochen.entities.Dept;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 董仁亮
 * @date 2020-02-19 23:39
 * @Description:
 */
@ControllerAdvice
public class GloablExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        // 记录错误信息
        String msg = e.getMessage();

        Dept dept=new Dept();
        dept.setDname("发生了异常");
        return dept;
    }

    @ResponseBody
    @ExceptionHandler(RequestNotPermitted.class)
    public Object handleException1(RequestNotPermitted e) {
        Dept dept=new Dept();
        dept.setDname("达到了限流");
        return dept;
    }
}
