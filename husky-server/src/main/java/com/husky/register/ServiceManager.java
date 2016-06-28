package com.husky.register;

import com.husky.bean.ServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by google on 16/5/27.
 */

public class ServiceManager {
    private  static final Logger logger = LoggerFactory.getLogger(ServiceManager.class);
    //注册参数
    private ServiceConfig serviceConfig;
    //服务map
    private Map<String,Object> serviceMap = new ConcurrentHashMap<String, Object>();
    //单列模式下的注册中心
    private static volatile ServiceManager serviceManager = new ServiceManager();
    private ServiceManager(){

    }
    public static ServiceManager getServiceManager(){
        return serviceManager;
    }

    public void addService(String serviceUrl,Object obj){
        serviceMap.put(serviceUrl, obj);
    }

    public int getServiceCount(){
        return serviceMap.size();
    }
    public Object getService(String serviceUrl){
        return serviceMap.get(serviceUrl);
    }

    public ServiceConfig getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    public Map<String, Object> getServiceMap() {
        return serviceMap;
    }
}
