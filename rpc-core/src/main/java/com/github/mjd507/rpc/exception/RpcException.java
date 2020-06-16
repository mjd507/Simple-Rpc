package com.github.mjd507.rpc.exception;

/**
 * Create by majiandong on 2020/6/16 10:34
 */
public class RpcException extends RuntimeException {

    public RpcException(String msg) {
        super(msg);
    }

    public RpcException(String message, Exception e) {
        super(message, e);
    }
}