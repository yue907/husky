package com.husky.netty;

import com.husky.common.codex.Decoder;
import com.husky.common.codex.Encoder;
import com.husky.register.ServiceManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by google on 16/6/25.
 */
public class RpcServer {
    private static final Logger logger =  LoggerFactory.getLogger(RpcServer.class);
//    private static final String IP = "0.0.0.0";
    private static final int PORT = 1234;
    /**
     * 用于分配处理业务线程的线程组个数
     */
    protected static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors() * 2;    //默认
    /**
     * 业务出现线程大小
     */
    protected static final int BIZTHREADSIZE = 4;
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);



    public static void run() throws Exception {
        final ServiceManager serviceManager = ServiceManager.getServiceManager();
        if (null == serviceManager)
            throw new IllegalArgumentException("service instance == null");
        if (serviceManager.getServiceConfig().getPort() <= 0 || serviceManager.getServiceConfig().getPort() > 65535)
            throw new IllegalArgumentException("Invalid port " + serviceManager.getServiceConfig().getPort());

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<SocketChannel>() {

            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                pipeline.addLast("decoder", new Decoder());
                pipeline.addLast("encoder", new Encoder());
                pipeline.addLast(new NettyServerHandler(serviceManager.getServiceMap()));
            }
        });
        b.option(ChannelOption.SO_BACKLOG, 1024);
        b.childOption(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture future = b.bind(PORT).sync();

//        b.bind(serviceManager.getServiceConfig().getPort()).sync();
//        logger.info("TCP服务器已启动");
        System.out.println("TCP服务器已启动");
//        future.channel().closeFuture().sync();
    }

    protected static void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
//        logger.info("开始启动TCP服务器...");
//        System.out.println("开始启动TCP服务器...");
        RpcServer.run();
//		TcpServer.shutdown();
    }
}