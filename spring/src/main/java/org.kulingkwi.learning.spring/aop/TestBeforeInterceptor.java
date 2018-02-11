package org.kulingkwi.learning.spring.aop;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class TestBeforeInterceptor implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("TestBeforeInterceptor running.........");
        System.out.println("target" + target.toString());
    }
}
