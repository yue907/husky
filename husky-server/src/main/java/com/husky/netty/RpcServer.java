package com.husky.netty;

import com.husky.common.codex.Decoder;
import com.husky.common.codex.Encoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by google on 16/6/25.
 */
public class RpcServer {
    private static final Logger logger =  LoggerFactory.getLogger(RpcServer.class);
    /**
     * 用于分配处理业务线程的线程组个数
     */
    protected static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors() * 2;
    /**
     * 业务出现线程大小
     */
    protected static final int BIZTHREADSIZE = 4;
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);



    public static void start(final int port,final Map<String,Object> serviceMap) throws Exception {

        if (port <= 0 || port > 65535)
            throw new IllegalArgumentException("Invalid port " + port);

        if(MapUtils.isEmpty(serviceMap)){
            throw new IllegalArgumentException("service instance == null");
        }
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<SocketChannel>() {

            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));//tcp数据分包、组包
                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));//tcp数据分包、组包
                pipeline.addLast("decoder", new Decoder());//解码器
                pipeline.addLast("encoder", new Encoder());//编码器
                pipeline.addLast(new NettyServerHandler(serviceMap));

            }
        });
        b.option(ChannelOption.SO_BACKLOG, 1024);
        b.childOption(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture future =  b.bind(port).sync();

//        future.channel().closeFuture().sync();
    }

    protected static void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }


}
