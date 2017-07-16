package com.husky.register.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.data.Stat;

/**
 * Created by google on 17/7/13.
 */
public class ZkClient {
    private CuratorFramework client;
    private volatile int retries = 3;

    private volatile int retryInterval = 3000;

    private volatile int retryLimit = 50;

    private int sessionTimeout = 30 * 1000;

    private int connectionTimeout = 15 * 1000;
    private String connectConfig;

    private static final String CHARSET = "UTF-8";


    public ZkClient(String connectConfig, int sessionTimeout, int connectionTimeout, int retries, int retryInterval) throws Exception{
        this.connectionTimeout = connectionTimeout;
        this.sessionTimeout = sessionTimeout;
        this.retries = retries;
        this.retryInterval = retryInterval;
        this.connectConfig = connectConfig;
        connect(connectConfig, sessionTimeout, connectionTimeout, retries, retryInterval);

    }

    public ZkClient(String connectConfig) throws Exception{
        this.connectConfig = connectConfig;
        connect(connectConfig, sessionTimeout, connectionTimeout, retries, retryInterval);
    }

    private void connect(String connectConfig, int sessionTimeout, int connectionTimeout, int retries, int retryInterval) {
        client = CuratorFrameworkFactory.builder().connectString(connectConfig)
                .sessionTimeoutMs(sessionTimeout).connectionTimeoutMs(connectionTimeout)
                .retryPolicy(new RetryNTimes(retries, retryInterval)).build();
        start();
    }

    private void start() {
        if (client.getState() != CuratorFrameworkState.STARTED) {
            client.start();
        }
    }

    public void create(String path, Object value) throws Exception {
        byte[] bytes = (value == null ? new byte[0] : value.toString().getBytes(CHARSET));
        client.create().creatingParentsIfNeeded().forPath(path, bytes);
    }

//    public void createEphemeral(String path, String value) throws Exception {
//        byte[] bytes = (value == null ? new byte[0] : value.toString().getBytes(CHARSET));
//        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, bytes);
//    }

    public void set(String path, Object value) throws Exception {
        byte[] bytes = (value == null ? new byte[0] : value.toString().getBytes(CHARSET));
        if (exists(path)) {
            client.setData().forPath(path, bytes);
        } else {
            client.create().creatingParentsIfNeeded().forPath(path, bytes);
        }
    }

    public void delete(String path) throws Exception {
        client.delete().forPath(path);
    }

    public String get(String path) throws Exception {
        if (exists(path)) {
            byte[] bytes = client.getData().forPath(path);
            String value = new String(bytes, CHARSET);
            return value;
        }
        return null;
    }

    public void close(){
        client.close();
    }

    public void destroy() throws Exception {
        synchronized (this) {
            if (client != null) {
                client.close();
            }
        }
    }

    public boolean exists(String path) throws Exception {
        Stat stat = client.checkExists().watched().forPath(path);
        return stat != null;
    }


}
