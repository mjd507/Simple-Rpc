package com.github.mjd507.rpc.client;

import com.github.mjd507.rpc.entity.RpcRequest;
import com.github.mjd507.rpc.entity.RpcResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Create by majiandong on 2020/6/16 17:48
 */
@Slf4j
public class ClientProxyHandler implements InvocationHandler {

    private NettyClient nettyClient;

    public ClientProxyHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("invoked method: [{}]", method.getName());
        RpcRequest rpcRequest = RpcRequest.builder()
                .requestId(UUID.randomUUID().toString())
                .methodName(method.getName())
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .build();
        CompletableFuture<RpcResponse> completableFuture = nettyClient.sendRequest(rpcRequest);
        RpcResponse rpcResponse = completableFuture.get();
        return rpcResponse.getData();
    }
}
