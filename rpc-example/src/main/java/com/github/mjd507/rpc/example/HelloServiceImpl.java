package com.github.mjd507.rpc.example;

/**
 * Create by majiandong on 2020/6/16 17:31
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public HelloEntity sayHello(HelloEntity entity) {
        entity.setName("-" + entity.getName() + "-");
        entity.setMsg("-" + entity.getName() + "-");
        entity.setAge(entity.getAge() + 1);
        return entity;
    }
}
