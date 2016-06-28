package com.husky.client.proxy;

import com.husky.client.bean.InvokerConfig;
import com.husky.client.netty.RpcClient;
import com.husky.common.bean.InvocationRequest;
import com.husky.common.bean.InvocationResponse;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.lang3.StringUtils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * Created by google on 16/6/23.
 */
public class CglibProxyFactory {
    public static <T> T getProxy(InvokerConfig invokerConfig) throws Exception {
        if (null == invokerConfig) {
            throw new IllegalArgumentException("InvokerConfig is null");
        }
        if (!invokerConfig.getInterClass().isInterface()) {
            throw new IllegalArgumentException("The " + invokerConfig.getInterClass().getName() + " must be interface class!");
        }
        if (StringUtils.isBlank(invokerConfig.getHost())) {
            throw new IllegalArgumentException("Host == null!");
        }
        if (invokerConfig.getPort() <= 0 || invokerConfig.getPort() > 65535) {
            throw new IllegalArgumentException("Invalid port " + invokerConfig.getPort());
        }

        System.out.println("Get remote service " + invokerConfig.getInterClass() + " from server " + invokerConfig.getHost() + ":" + invokerConfig.getPort());

        final String host = invokerConfig.getHost();
        final int port = invokerConfig.getPort();
        final String serviceUrl = invokerConfig.getServiceUrl();
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{invokerConfig.getInterClass()});
        enhancer.setCallback(new MethodInterceptor() {
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//                return null;
//                Socket socket = new Socket(host, port);
//                try {
//                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
//                    try {
//                        //依次写入需要调用的方法名，参数类型，参数
                        InvocationRequest request = new InvocationRequest();
                        request.setArguments(objects);
                        request.setServiceUrl(serviceUrl);
                        request.setArgumentsType(method.getParameterTypes());
                        request.setMethodName(method.getName());
//                        output.writeObject(request);
//
//                        //读取返回对象
//                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
//                        try {
//                            Object result = input.readObject();
//                            if (result instanceof Throwable) {
//                                throw (Throwable) result;
//                            }
//                            return result;
//                        } finally {
//                            input.close();
//                        }
//                    } finally {
//                        output.close();
//                    }
//                } finally {
//                    socket.close();
//                }
                try {
                    RpcClient client = new RpcClient(host,port);
                    InvocationResponse response = client.send(request);
                    if(null != response.getResult()){
                        return response.getResult();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        return (T)enhancer.create();
    }
}
