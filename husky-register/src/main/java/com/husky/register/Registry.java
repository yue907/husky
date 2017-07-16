package com.husky.register;

/**
 * 服务注册接口
 * Created by google on 17/7/14.
 */
public interface Registry {
    /**
     * 初始化
     */
    void init();

    /**
     * 获取服务地址
     * @param serviceName
     * @return
     * @throws Exception
     */
    String getServiceAddress(String serviceName) throws Exception;

    /**
     * 获取服务地址
     * @param serviceName
     * @param group
     * @return
     * @throws Exception
     */
    String getServiceAddress(String serviceName,String group) throws Exception;

    /**
     * 注册服务地址
     * @param serviceName
     * @param serviceAddress
     * @throws Exception
     */
    void registerService(String serviceName, String serviceAddress) throws Exception;

    /**
     * 注册服务地址
     * @param serviceName
     * @param group
     * @param serviceAddress
     * @throws Exception
     */
    void registerService(String serviceName, String group,String serviceAddress) throws Exception;

    /**
     * 销毁
     */
    void destory();

    String getName();
}
