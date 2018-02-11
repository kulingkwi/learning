package org.kulingkwi.learning.spring.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class TestInterceptor implements  MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long beginTime=System.currentTimeMillis();
        invocation.proceed();
        long endTime=System.currentTimeMillis();
        String targetMethodName=invocation.getMethod().getName();
        String logInfoText="环绕通知: "+targetMethodName+"方法调用前时间"+beginTime+"毫秒,"+"调用后时间"+endTime+"毫秒";
        System.out.println(logInfoText);
        return null;
    }
}
