package com.example.demo.crosscut.src;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
    @Before("execution(* com.example.demo.crosscut.src.service.*.*(..))")
    public void logBefore() {
        System.out.println("AOP: 메서드 실행 전 로그");
    }
}
