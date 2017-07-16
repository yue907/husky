package com.husky.register.zookeeper;

import com.husky.register.Registry;
import com.husky.register.util.PathUtil;
import org.apache.commons.lang3.StringUtils;


/**
 * 服务注册接口--zk实现
 * Created by google on 17/7/14.
 */
public class CuratorRegistry implements Registry {
    private ZkClient zkClient;
    private volatile boolean inited = false;
    private static final String connectConfig = "127.0.0.1:4081";
    public static final String REGISTRY_CURATOR_NAME = "curator";

    /**
     * 初始化zk客户端
     */
    public void init() {
        if (!inited) {
            synchronized (this) {
                if (!inited) {
                    try {
                        zkClient = new ZkClient(connectConfig);
                        inited = true;
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }

    /**
     * 获取服务地址
     *
     * @param serviceName
     * @return
     * @throws Exception
     */
    public String getServiceAddress(String serviceName) throws Exception {
//        if (StringUtils.isBlank(serviceName)) {
//            throw new IllegalArgumentException("service name can not be blank");
//        }
//        String path = PathUtil.getServicePath(serviceName,null);
//        String address = zkClient.get(path);
//        return address;
        return getServiceAddress(serviceName,null);
    }


    /**
     * 注册服务地址
     *
     * @param serviceName
     * @param serviceAddress
     * @throws Exception
     */
    public void registerService(String serviceName, String serviceAddress) throws Exception {
//        if (StringUtils.isBlank(serviceName) || StringUtils.isBlank(serviceAddress)) {
//            throw new IllegalArgumentException("param can not be blank");
//        }
//        StringBuffer sb = new StringBuffer();
//        String path = PathUtil.getServicePath(serviceName,null);
//        if (zkClient.exists(path)) {
//            String oldAddress = zkClient.get(path);
//            if (StringUtils.isNotBlank(oldAddress)) {
//                sb.append(oldAddress).append(",");
//            }
//        }
//        sb.append(serviceAddress);
//        zkClient.set(path, sb.toString());
        registerService(serviceName,null,serviceAddress);
    }

    /**
     * 获取服务地址
     *
     * @param serviceName
     * @param group
     * @return
     * @throws Exception
     */
    public String getServiceAddress(String serviceName, String group) throws Exception {
        if (StringUtils.isBlank(serviceName)) {
            throw new IllegalArgumentException("service name can not be blank");
        }
        String path = PathUtil.getServicePath(serviceName,group);
        String address = zkClient.get(path);
        return address;
    }

    /**
     * 注册服务地址
     *
     * @param serviceName
     * @param group
     * @param serviceAddress
     * @throws Exception
     */
    public void registerService(String serviceName, String group, String serviceAddress) throws Exception {
        if (StringUtils.isBlank(serviceName) || StringUtils.isBlank(serviceAddress)) {
            throw new IllegalArgumentException("param can not be blank");
        }
        StringBuffer sb = new StringBuffer();
        String path = PathUtil.getServicePath(serviceName, group);
        if (zkClient.exists(path)) {
            String oldAddress = zkClient.get(path);
            if (StringUtils.isNotBlank(oldAddress)) {//重复注册还未处理
//                String[] addresses = oldAddress.split(",");
//                for(String address : addresses){
//                    if(sameAddress(address,serviceAddress)){
//                        continue;
//                    }
//                    sb.append(address).append(",");
//                }
                sb.append(oldAddress).append(",");
            }
        }
        sb.append(serviceAddress);
        zkClient.set(path, sb.toString());
    }

    public boolean sameAddress(String address1,String address2){
        if(StringUtils.isBlank(address1) || StringUtils.isBlank(address2)){
            return false;
        }

        if(address1.equals(address2)){
            return true;
        }
        if(address1.substring(0,address1.indexOf(":")).equals(address2.substring(0,address1.indexOf(":")))){
            return true;
        }
        return false;
    }

    /**
     * 销毁客户端
     */
    public void destory() {
        zkClient.close();
    }

    public String getName() {
        return REGISTRY_CURATOR_NAME;
    }
}
