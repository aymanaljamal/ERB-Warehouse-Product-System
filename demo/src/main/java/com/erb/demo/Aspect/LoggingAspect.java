package com.erb.demo.Aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    // ===== CONTROLLER LOGGING =====
    @Before("execution(* com.erb.demo.controller..*(..))")
    public void logBeforeController(JoinPoint joinPoint) {
        System.out.println(">>> ENTER Controller Method: " + joinPoint.getSignature());
    }

    @AfterReturning(pointcut = "execution(* com.erb.demo.controller..*(..))", returning = "result")
    public void logAfterController(JoinPoint joinPoint, Object result) {
        System.out.println("<<< EXIT Controller Method: " + joinPoint.getSignature());
        System.out.println("Returned from Controller: " + result);
    }

    @AfterThrowing(pointcut = "execution(* com.erb.demo.controller..*(..))", throwing = "ex")
    public void logControllerException(JoinPoint joinPoint, Throwable ex) {
        System.out.println("!!! EXCEPTION in Controller: " + joinPoint.getSignature());
        System.out.println("Exception message: " + ex.getMessage());
    }

    // ===== SERVICE LOGGING =====
    @Before("execution(* com.erb.demo.service..*(..))")
    public void logBeforeService(JoinPoint joinPoint) {
        System.out.println(">>> ENTER Service Method: " + joinPoint.getSignature());
    }

    @AfterReturning(pointcut = "execution(* com.erb.demo.service..*(..))", returning = "result")
    public void logAfterService(JoinPoint joinPoint, Object result) {
        System.out.println("<<< EXIT Service Method: " + joinPoint.getSignature());
        System.out.println("Returned from Service: " + result);
    }

    @AfterThrowing(pointcut = "execution(* com.erb.demo.service..*(..))", throwing = "ex")
    public void logServiceException(JoinPoint joinPoint, Throwable ex) {
        System.out.println("!!! EXCEPTION in Service: " + joinPoint.getSignature());
        System.out.println("Exception message: " + ex.getMessage());
    }

    // ===== OPTIONAL: LOGGING REPOSITORY =====
    @Before("execution(* com.erb.demo.repository..*(..))")
    public void logBeforeRepository(JoinPoint joinPoint) {
        System.out.println(">>> ENTER Repository Method: " + joinPoint.getSignature());
    }

    @AfterThrowing(pointcut = "execution(* com.erb.demo.repository..*(..))", throwing = "ex")
    public void logRepositoryException(JoinPoint joinPoint, Throwable ex) {
        System.out.println("!!! EXCEPTION in Repository: " + joinPoint.getSignature());
        System.out.println("Exception message: " + ex.getMessage());
    }
}
