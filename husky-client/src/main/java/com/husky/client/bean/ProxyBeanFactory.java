package com.husky.client.bean;

import com.husky.client.proxy.CglibProxyFactory;
import com.husky.client.proxy.ProxyFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by google on 16/5/27.
 */
public class ProxyBeanFactory implements FactoryBean{
    private final Logger logger = LoggerFactory.getLogger(ProxyBeanFactory.class);
    //接口名
    private String interName;
    //接口类
    private Class interClass;
    //服务名
    private String serviceUrl;
    //代理实现类
    private Object refObjexct;
    //类加载器
    private ClassLoader classLoader;
    //ip地址
    private String host;
    //端口
    private int port;

    public ProxyBeanFactory(){
        this.classLoader = ProxyBeanFactory.class.getClassLoader();
    }

    public void init() throws Exception{
        if(StringUtils.isBlank(interName)){
            throw new IllegalArgumentException("illegal interface:"+interName);
        }
        interClass = classLoader.loadClass(interName.trim());
        InvokerConfig invokerConfig = new InvokerConfig(serviceUrl.trim(),interClass,host.trim(),port);
//        this.refObjexct = ProxyFactory.getProxy(invokerConfig);
        this.refObjexct = CglibProxyFactory.getProxy(invokerConfig);
    }

    public Object getObject() throws Exception {
        return refObjexct;
    }

    public Class<?> getObjectType() {
        return null;
    }

    public boolean isSingleton() {
        return false;
    }

    public String getInterName() {
        return interName;
    }

    public void setInterName(String interName) {
        this.interName = interName;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
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
