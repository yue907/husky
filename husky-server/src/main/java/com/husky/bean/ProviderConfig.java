package com.husky.bean;

/**
 * Created by google on 17/7/16.
 */
public class ProviderConfig<T> {
    private int port;
//    private Class<?> serviceInterface;
    private String url;
    private T service;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

//    public Class<?> getServiceInterface() {
//        return serviceInterface;
//    }
//
//    public void setServiceInterface(Class<?> serviceInterface) {
//        this.serviceInterface = serviceInterface;
//    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public T getService() {
        return service;
    }

    public void setService(T service) {
        this.service = service;
    }
}
