package com.dan.spring.boot.nexus.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 面向切片,只能监测ioc容器里面的对象
 */
@Component
@Aspect
public class LogAspect {
    @Pointcut("execution(* com.spring.example.userservice.pojo.*.*(..))")
    public void pc1() {

    }

    @Before(value = "pc1()")
    public void before(JoinPoint point) {
        String name = point.getSignature().getName();
        System.out.println("before:" + name);
    }

    @Before(value = "pc1()")
    public void after(JoinPoint point) {
        String name = point.getSignature().getName();
        System.out.println("after:" + name);
    }

    @AfterReturning(value = "pc1()", returning = "result")
    public void afterReturn(JoinPoint point, Object result) {
        String name = point.getSignature().getName();
        System.out.println("afterReturn:" + name + "的返回值:" + result);
    }

    @AfterThrowing(value = "pc1()", throwing = "e")
    public void afterThrowing(JoinPoint point, Exception e) {

    }
}
