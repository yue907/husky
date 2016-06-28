package com.husky.register;

import com.husky.bean.ServiceConfig;
import com.husky.netty.RpcServer;

import java.util.Map;

/**
 * Created by google on 16/5/27.
 */
public class ServiceFactoryBean {
    //端口
    private int port;
    //服务Map
    private Map<String,Object> services;

    public ServiceFactoryBean(){
        this.port = 1234;
    }

    public void init() throws Exception{
        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setPort(port);
        ServiceManager serviceManager = ServiceManager.getServiceManager();
        serviceManager.setServiceConfig(serviceConfig);
        for(Map.Entry<String,Object> entry : services.entrySet()){
            serviceManager.addService(entry.getKey(),entry.getValue());//服务注册
        }
        publish();

    }
    //服务发布
    public void publish() throws Exception{
//        ServicePublish.start();
        RpcServer.run();
    }
    public Map<String, Object> getServices() {
        return services;
    }

    public void setServices(Map<String, Object> services) {
        this.services = services;
    }
}
