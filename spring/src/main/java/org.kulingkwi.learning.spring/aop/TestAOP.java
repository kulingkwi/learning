package org.kulingkwi.learning.spring.aop;

import org.kulingkwi.learning.spring.ioc.TestBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestAOP {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-aop-test.xml");
        TestInterface testBean = (TestInterface) applicationContext.getBean("testBean");
        System.out.println(testBean.getName());
    }
}
