package com.husky.client.proxy;

import com.husky.client.bean.InvokerConfig;
import org.apache.commons.lang3.StringUtils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * Created by google on 16/5/27.
 */
public class ProxyFactory {
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
        //用java的原生动态代理，构建代理类
        return (T) Proxy.newProxyInstance(invokerConfig.getInterClass().getClassLoader(), new Class<?>[]{invokerConfig.getInterClass()}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
                //构建socket 发送请求
                Socket socket = new Socket(host, port);
                try {
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    try {
                        //依次写入需要调用的方法名，参数类型，参数
                        output.writeUTF(serviceUrl);
                        output.writeUTF(method.getName());
                        output.writeObject(method.getParameterTypes());
                        output.writeObject(arguments);

                        //读取返回对象
                        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                        try {
                            Object result = input.readObject();
                            if (result instanceof Throwable) {
                                throw (Throwable) result;
                            }
                            return result;
                        } finally {
                            input.close();
                        }
                    } finally {
                        output.close();
                    }
                } finally {
                    socket.close();
                }
            }
        });
    }
}
