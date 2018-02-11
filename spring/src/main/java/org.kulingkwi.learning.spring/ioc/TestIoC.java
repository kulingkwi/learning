package org.kulingkwi.learning.spring.ioc;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

public class TestIoC {
    public static void main(String[] args) {
        ClassPathResource resource = new ClassPathResource("spring-ioc-test.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(resource);

        TestBean bean = (TestBean) factory.getBean("test");
        System.out.println(bean.getName());

    }
}
