package com.husky.register;

import com.husky.common.bean.InvocationRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * Created by google on 16/5/27.
 */
public class ServicePublish {
    public static void start()throws Exception{
        final ServiceManager serviceManager = ServiceManager.getServiceManager();
        if (null == serviceManager)
            throw new IllegalArgumentException("service instance == null");
        if (serviceManager.getServiceConfig().getPort() <= 0 || serviceManager.getServiceConfig().getPort() > 65535)
            throw new IllegalArgumentException("Invalid port " + serviceManager.getServiceConfig().getPort());

        ServerSocket server = new ServerSocket(serviceManager.getServiceConfig().getPort());
        for (; ; ) {
            try {
                final Socket socket = server.accept();
                new Thread(new Runnable() {

                    public void run() {
                        try {
                            try {
                                //使用对象流
                                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                                try {
                                    InvocationRequest request = (InvocationRequest) input.readObject();
                                    //按照序列化的顺序，依次读取要调用的 方法 参数类型 参数
                                    String serviceUrl = request.getServiceUrl();
                                    String methodName = request.getMethodName();
                                    Class<?>[] parameterTypes = (Class<?>[]) request.getArgumentsType();
                                    Object[] arguments = (Object[]) request.getArguments();

                                    //构建返回流
                                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

                                    try {
                                        Object serviceBean = serviceManager.getService(serviceUrl);
                                        Class<?> serviceClass = serviceBean.getClass();
                                        //反射获取方法
                                        Method method = serviceClass.getMethod(methodName, parameterTypes);
                                        //调用方法
                                        Object result = method.invoke(serviceBean, arguments);
                                        //调用结果写入返回流
                                        output.writeObject(result);
                                    } catch (Throwable t) {
                                        output.writeObject(t);
                                    } finally {
                                        output.close();
                                    }
                                } finally {
                                    input.close();
                                }
                            } finally {
                                socket.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
