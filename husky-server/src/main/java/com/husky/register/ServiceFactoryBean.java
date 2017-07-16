package com.husky.register;

import com.husky.bean.ProviderConfig;
import com.husky.netty.RpcServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by google on 16/5/27.
 */
public class ServiceFactoryBean {
    //端口
    private int port = 1234;
    //服务Map,key是服务远程URL，value是服务的实现类
    private Map<String,Object> services;



    public void init() throws Exception{
        List<ProviderConfig<?>> providerConfigList = new ArrayList<ProviderConfig<?>>();
        for(Map.Entry<String,Object> entry : services.entrySet()){

            ProviderConfig<Object> providerConfig = new ProviderConfig<Object>();
            providerConfig.setPort(port);
            providerConfig.setService(entry.getValue());
            providerConfig.setUrl(entry.getKey());
           providerConfigList.add(providerConfig);
        }
        publish();
        ServicePublisher.publishService(providerConfigList);

    }
    //服务发布
    public void publish() throws Exception{
        RpcServer.start(port,services);
    }
    public Map<String, Object> getServices() {
        return services;
    }

    public void setServices(Map<String, Object> services) {
        this.services = services;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
