package org.example.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyTestService implements InvocationHandler {


    private TestService testService;


    public ProxyTestService(TestService testService) {
        this.testService = testService;
    }


    public TestService getInstance() {
        Object o = Proxy.newProxyInstance(testService.getClass().getClassLoader(), testService.getClass().getInterfaces(), this);
        return (TestService) o;
    }


    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        String name = method.getName();
        System.out.println("method name is "+name);
        Object invoke = method.invoke(testService, objects);
        return invoke;
    }
}
