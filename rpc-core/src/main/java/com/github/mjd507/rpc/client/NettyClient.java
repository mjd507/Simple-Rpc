package com.github.mjd507.rpc.client;

import com.github.mjd507.rpc.entity.RpcRequest;
import com.github.mjd507.rpc.entity.RpcResponse;
import com.github.mjd507.rpc.serial.KryoMsgDecoder;
import com.github.mjd507.rpc.serial.KryoMsgEncoder;
import com.github.mjd507.rpc.serial.KryoSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * netty 客户端
 * Create by majiandong on 2020/6/16 11:15
 */
@Slf4j
@Getter
@Setter
public class NettyClient {
    private String host;
    private int port;

    private Bootstrap bootstrap;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        KryoSerializer kryoSerializer = new KryoSerializer();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        // RpcResponse -> ByteBuf
                        ch.pipeline().addLast(new KryoMsgDecoder(kryoSerializer, RpcResponse.class));
                        // ByteBuf -> RpcRequest
                        ch.pipeline().addLast(new KryoMsgEncoder(kryoSerializer, RpcRequest.class));
                        ch.pipeline().addLast(new ClientHandler(NettyClient.this));
                    }
                });
    }

    private final Map<String, CompletableFuture<RpcResponse>> requestMap = new ConcurrentHashMap<>();

    public CompletableFuture<RpcResponse> removeReq(String key) {
        return requestMap.remove(key);
    }

    private final Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    private Channel getChannel() {
        String key = this.host + ":" + this.port;
        if (channelMap.containsKey(key)) {
            Channel channel = channelMap.get(key);
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                channelMap.remove(key);
            }
        }
        Channel channel = this.connect();
        channelMap.put(key, channel);
        return channel;
    }

    @SneakyThrows
    private Channel connect() {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(this.host, this.port).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("客户端连接成功!");
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }

    public CompletableFuture<RpcResponse> sendRequest(RpcRequest rpcRequest) {
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        Channel channel = getChannel();
        if (channel != null && channel.isActive()) {
            try {
                requestMap.put(rpcRequest.getRequestId(), resultFuture);
                ChannelFuture future = channel.writeAndFlush(rpcRequest).sync();
                if (future.isSuccess()) {
                    log.info("client send message: [{}]", rpcRequest);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("Send failed:", future.cause());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalStateException();
        }
        return resultFuture;
    }


}
