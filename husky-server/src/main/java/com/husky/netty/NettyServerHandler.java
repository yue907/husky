package com.husky.netty;

import com.husky.common.bean.InvocationResponse;
import io.netty.channel.*;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import com.husky.common.bean.InvocationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
/**
 * Created by google on 16/6/24.
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<InvocationRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    private final Map<String, Object> serviceMap;

    public NettyServerHandler(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, InvocationRequest request) throws Exception {
        InvocationResponse response = new InvocationResponse();
//        response.setRequestId(request.getRequestId());
        try {
            System.out.println("读取request");
            Object result = handle(request);
//            Object result = "guge";
            System.out.println("处理request，result"+result);
            response.setResult(result);
        } catch (Throwable t) {
            response.setError(t);
        }
        System.out.println("返回response");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Object handle(InvocationRequest request) throws Throwable {
        String serviceUrl = request.getServiceUrl();
        Object serviceBean = serviceMap.get(serviceUrl);
        if(null == serviceBean){
            logger.warn("serviceUrl not found:"+request.getServiceUrl());
            return null;
        }
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getArgumentsType();
        Object[] parameters = request.getArguments();

        /*Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);*/

        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("server caught exception", cause);
        ctx.close();
    }
}