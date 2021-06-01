package org.example.service.impl;

import org.example.service.TestService;

public class TestServiceImpl implements TestService {
    @Override
    public void say() {
        System.out.println("say ------");
    }

    @Override
    public void hello() {
        System.out.println("hello ------");

    }
}
