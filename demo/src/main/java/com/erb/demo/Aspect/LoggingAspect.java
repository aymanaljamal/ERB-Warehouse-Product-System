package com.erb.demo.Aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.erb.demo.service.*.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        System.out.println("Before executing method: " + joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* com.erb.demo.service.*.*(..))", returning = "result")
    public void afterMethod(JoinPoint joinPoint, Object result) {
        System.out.println("After executing method: " + joinPoint.getSignature().getName());
        System.out.println("Returned result: " + result);
    }
}
