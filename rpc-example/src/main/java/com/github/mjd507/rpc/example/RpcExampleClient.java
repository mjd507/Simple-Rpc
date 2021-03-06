package com.github.mjd507.rpc.example;

import com.github.mjd507.rpc.client.ClientProxyHandler;
import com.github.mjd507.rpc.client.NettyClient;

/**
 * Create by majiandong on 2020/6/16 17:38
 */
public class RpcExampleClient {

    public static void main(String[] args) {
        NettyClient client = new NettyClient("127.0.0.1", 9005);
        client.start();
        // send request
        ClientProxyHandler proxyHandler = new ClientProxyHandler(client);
        HelloService helloService = proxyHandler.getProxy(HelloService.class);
        HelloEntity response = helloService.sayHello(new HelloEntity("bug", 10, "hhh"));
        System.out.println(response);
    }
}
