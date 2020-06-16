package com.github.mjd507.rpc.server;

import com.github.mjd507.rpc.exception.RpcException;
import com.github.mjd507.util.http.ApiCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Create by majiandong on 2020/6/16 19:49
 */
@Slf4j
public class ServiceProvider {

    private ServiceProvider() {
    }

    private static Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    public static <T> void addServiceProvider(T service, Class<T> serviceClass) {
        String serviceName = serviceClass.getCanonicalName();
        if (serviceMap.containsKey(serviceName)) {
            return;
        }
        serviceMap.put(serviceName, service);
        log.info("Add service: {} and interfaces:{}", serviceName, service.getClass().getInterfaces());
    }

    public static Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (null == service) {
            throw new RpcException(ApiCode.BAD_REQUEST.getDesc());
        }
        return service;
    }
}
