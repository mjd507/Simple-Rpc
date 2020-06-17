package com.github.mjd507.rpc.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Create by majiandong on 2020/6/15 16:40
 */
@Getter
@Setter
@ToString
public class RpcRequest {
    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
}
