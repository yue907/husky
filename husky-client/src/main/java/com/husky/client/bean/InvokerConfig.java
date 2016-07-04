package com.husky.client.bean;

/**
 * Created by google on 16/5/27.
 */
public class InvokerConfig {
    //服务连接
    private String serviceUrl;
    //接口
    private Class interClass;
    //ip地址
    private String host;
    //端口
    private int port;

    public InvokerConfig(String serviceUrl,Class interClass,String host,int port){
        this.serviceUrl = serviceUrl;
        this.interClass = interClass;
        this.host = host;
        this.port = port;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public Class getInterClass() {
        return interClass;
    }

    public void setInterClass(Class interClass) {
        this.interClass = interClass;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
