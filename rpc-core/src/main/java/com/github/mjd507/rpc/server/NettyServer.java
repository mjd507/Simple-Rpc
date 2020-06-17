package com.github.mjd507.rpc.server;

import com.github.mjd507.rpc.entity.RpcRequest;
import com.github.mjd507.rpc.entity.RpcResponse;
import com.github.mjd507.rpc.serial.KryoMsgDecoder;
import com.github.mjd507.rpc.serial.KryoMsgEncoder;
import com.github.mjd507.rpc.serial.KryoSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * netty 服务端
 * 接受请求，处理请求
 * Create by majiandong on 2020/6/15 17:08
 */
public class NettyServer {

    private int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void start() {
        KryoSerializer kryoSerializer = new KryoSerializer();
        //EventLoopGroup 是用来处理IO操作的多线程事件循环器
        // bossGroup 用来接收进来的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //workerGroup 用来处理已经被接收的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //启动 NIO 服务的辅助启动类
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {

                            ch.pipeline().addLast(new KryoMsgDecoder(kryoSerializer, RpcRequest.class));
                            ch.pipeline().addLast(new KryoMsgEncoder(kryoSerializer, RpcResponse.class));
                            // 注册handler
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // 绑定端口，开始接收进来的连接
            ChannelFuture f = bootstrap.bind(this.port).sync();
            // 等待服务器 socket 关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}

