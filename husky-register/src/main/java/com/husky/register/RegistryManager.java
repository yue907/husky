
package com.husky.register;


import com.husky.common.extension.ExtensionLoader;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class RegistryManager {
    //单例
    private static final RegistryManager instance = new RegistryManager();
    private static volatile boolean isInit = false;
    private static volatile Registry registry = null;
    private static final String default_registry = "curator";

    private RegistryManager() {
    }

    public static RegistryManager getInstance() {
        if (!isInit) {
            synchronized (RegistryManager.class) {
                if (!isInit) {
                    instance.init();
                    isInit = true;
                }
            }
        }
        return instance;
    }

    private void init() {
        List<Registry> registryList = ExtensionLoader.getExtensionList(Registry.class);
        try {
            if (CollectionUtils.isEmpty(registryList)) {
                throw new RuntimeException("failed to find registry extension type, please check dependencies!");
            }


            for (Registry registry : registryList) {
                if (registry.getName().equals(default_registry)) {
                    registry.init();
                    RegistryManager.registry = registry;
                }
            }


        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public void registerService(String serviceName, String serviceAddress)
            throws Exception {
        registerService(serviceName, null, serviceAddress, 1);
    }

    public void registerService(String serviceName, String group, String serviceAddress, int weight)
            throws Exception {
        if (registry != null) {
            registry.registerService(serviceName, group, serviceAddress);
        }
    }

    public String getServiceAddress(String serviceName, String group) throws Exception {
        if (null != registry) {
            return registry.getServiceAddress(serviceName, group);
        }
        return "";
    }
}
