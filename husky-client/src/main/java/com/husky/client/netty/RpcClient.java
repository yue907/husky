package com.husky.client.netty;

import com.husky.common.bean.InvocationRequest;
import com.husky.common.bean.InvocationResponse;
import com.husky.common.codex.Decoder;
import com.husky.common.codex.Encoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by google on 16/7/1.
 */
public class RpcClient extends SimpleChannelInboundHandler<InvocationResponse> {
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    private String host;
    private int port;
    private InvocationResponse response;
    private final Object obj = new Object();
    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InvocationResponse msg) throws Exception {
        this.response = msg;
        synchronized (obj) {
            obj.notifyAll(); // 收到响应，唤醒线程
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("client caught exception", cause);
        ctx.close();
    }
    public InvocationResponse send(InvocationRequest request) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                                    .addLast("frameEncoder", new LengthFieldPrepender(4))
                                    .addLast(new Encoder()) // 编码器
                                    .addLast(new Decoder()) // 解码器
                                    .addLast(RpcClient.this); // 使用 RpcClient 发送 RPC 请求
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().writeAndFlush(request).sync();
            synchronized (obj) {
                obj.wait(); // 等待响应
            }
            future.channel().closeFuture();
            return response;
        } finally {
            group.shutdownGracefully();
        }
    }
}
