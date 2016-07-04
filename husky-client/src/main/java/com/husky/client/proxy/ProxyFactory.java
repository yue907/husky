package com.husky.client.proxy;

import com.husky.client.bean.InvokerConfig;
import com.husky.client.netty.RpcClient;
import com.husky.common.bean.InvocationRequest;
import com.husky.common.bean.InvocationResponse;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * Created by google on 16/6/23.
 */
public class ProxyFactory {
    public static <T> T getProxy(InvokerConfig invokerConfig) throws Exception {
        isPositive(invokerConfig);
        final String host = invokerConfig.getHost();
        final int port = invokerConfig.getPort();
        final String serviceUrl = invokerConfig.getServiceUrl();
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{invokerConfig.getInterClass()});
        enhancer.setCallback(new MethodInterceptor() {
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

                        InvocationRequest request = new InvocationRequest();
                        request.setArguments(objects);
                        request.setServiceUrl(serviceUrl);
                        request.setArgumentsType(method.getParameterTypes());
                        request.setMethodName(method.getName());
                try {
                    RpcClient client = new RpcClient(host,port);
                    Object response = client.send(request);
                    InvocationResponse result = (InvocationResponse) response;
                    return result.getResult();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        return (T)enhancer.create();
    }

    /**
     * @Description: 判断参数是否合法
     * @param invokerConfig
     */
    private static void isPositive(InvokerConfig invokerConfig){
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
    }
}
