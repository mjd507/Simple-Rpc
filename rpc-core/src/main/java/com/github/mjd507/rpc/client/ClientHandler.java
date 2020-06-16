package com.github.mjd507.rpc.client;

import com.github.mjd507.rpc.entity.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * Create by majiandong on 2020/6/16 11:16
 */
@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private NettyClient nettyClient;

    public ClientHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            log.info("client receive msg: [{}]", msg);
            RpcResponse rpcResponse = (RpcResponse) msg;
            CompletableFuture<RpcResponse> future = nettyClient.removeReq(rpcResponse.getRequestId());
            if (null != future) {
                future.complete(rpcResponse);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
