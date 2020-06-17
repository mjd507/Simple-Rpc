package com.github.mjd507.rpc.entity;

import com.github.mjd507.util.http.ApiCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Create by majiandong on 2020/6/15 16:40
 */
@ToString
@Setter
@Getter
public class RpcResponse {
    private String requestId;
    private int code;
    private Object data;
    private String msg;

    public RpcResponse() {
    }

    public RpcResponse(String requestId, int code, Object data, String msg) {
        this.requestId = requestId;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static RpcResponse success(String requestId, Object result) {
        return new RpcResponse(requestId, ApiCode.OK.getCode(), result, ApiCode.OK.getDesc());
    }
}
