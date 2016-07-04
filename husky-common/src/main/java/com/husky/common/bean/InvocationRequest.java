package com.husky.common.bean;

import java.io.Serializable;

/**
 * RPC客户端请求实体类
 * Created by google on 16/6/23.
 */
public class InvocationRequest implements Serializable {
    private static final long serialVersionUID = 5868293976875578768L;
    //服务连接
    private String serviceUrl;
    //方法名
    private String methodName;
    //参数类型列表
    private Class[] argumentsType;
    // 参数列表
    private Object[] arguments;
    //调用的接口名
    private Class<?> itf;

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getArgumentsType() {
        return argumentsType;
    }

    public void setArgumentsType(Class[] argumentsType) {
        this.argumentsType = argumentsType;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public Class<?> getItf() {
        return itf;
    }

    public void setItf(Class<?> itf) {
        this.itf = itf;
    }
}