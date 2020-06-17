package com.github.mjd507.rpc.example;

import com.github.mjd507.rpc.server.NettyServer;
import com.github.mjd507.rpc.server.ServerProxyProvider;

/**
 * Create by majiandong on 2020/6/16 17:35
 */
public class RpcExampleServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServerProxyProvider.addProxy(helloService, HelloService.class);

        NettyServer nettyServer = new NettyServer(9005);
        nettyServer.start();


    }
}
