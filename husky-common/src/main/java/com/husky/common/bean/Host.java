package com.husky.common.bean;

import java.io.Serializable;

/**
 * Created by google on 16/7/5.
 */
public class Host implements Serializable{
    private static final long serialVersionUID = -2539098409373394074L;
    String ip;
    int port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
