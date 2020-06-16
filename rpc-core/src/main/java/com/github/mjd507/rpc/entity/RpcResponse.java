package com.github.mjd507.rpc.entity;

import com.github.mjd507.util.http.ApiCode;
import com.github.mjd507.util.http.ApiResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Create by majiandong on 2020/6/15 16:40
 */
@ToString
@Setter
@Getter
public class RpcResponse extends ApiResponse {
    private String requestId;

    public RpcResponse(int code, Object data, String msg) {
        super(code, data, msg);
    }

    public static RpcResponse success(String requestId, Object result) {
        ApiResponse apiResponse = ApiResponse.ok(result);
        RpcResponse rpcResponse = (RpcResponse) apiResponse;
        rpcResponse.requestId = requestId;
        return rpcResponse;
    }

    public static RpcResponse error(String requestId, ApiCode apiCode) {
        ApiResponse apiResponse = ApiResponse.error(apiCode);
        RpcResponse rpcResponse = (RpcResponse) apiResponse;
        rpcResponse.requestId = requestId;
        return rpcResponse;
    }

}
