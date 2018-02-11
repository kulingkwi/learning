package org.kulingkwi.learning.spring.aop;

public class TestImpl implements TestInterface{

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
