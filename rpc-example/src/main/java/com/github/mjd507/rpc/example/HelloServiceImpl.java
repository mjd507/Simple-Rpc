package com.github.mjd507.rpc.example;

/**
 * Create by majiandong on 2020/6/16 17:31
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String msg) {
        return "收到:" + msg;
    }
}
