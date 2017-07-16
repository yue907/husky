package com.husky.client;

import com.husky.client.bean.ServerConfig;
import com.husky.register.RegistryManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * Created by google on 17/7/16.
 */
public class ClientManage {
    public static ServerConfig getServerConfig(String serviceUrl) throws Exception{
        String addresses = RegistryManager.getInstance().getServiceAddress(serviceUrl,null);
        if(StringUtils.isBlank(addresses)){
            throw new IllegalArgumentException("address not find");
        }
        String[] addressList = addresses.split(",");
        for(String address : addressList){
            String[] cofing = address.split(":");
            ServerConfig serverConfig = new ServerConfig();
            serverConfig.setIp(cofing[0]);
            serverConfig.setPort(NumberUtils.toInt(cofing[1]));
            return serverConfig;
        }
        return null;
    }
}
