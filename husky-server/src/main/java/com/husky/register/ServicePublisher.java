package com.husky.register;

import com.husky.bean.ProviderConfig;
import org.apache.commons.collections4.CollectionUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by google on 17/7/16.
 */
public class ServicePublisher {
    public static void publishService(List<ProviderConfig<?>> providerConfigList) throws Exception{
        if(CollectionUtils.isEmpty(providerConfigList)){
            throw new IllegalArgumentException("server provider config can not be empty");
        }
        String localIp = getLocalIp();
        for(ProviderConfig providerConfig : providerConfigList){
            RegistryManager.getInstance().registerService(providerConfig.getUrl(),localIp+":"+providerConfig.getPort());
        }
    }

    private static String getLocalIp() throws UnknownHostException{
        return InetAddress.getLocalHost().getHostAddress();
    }
}
