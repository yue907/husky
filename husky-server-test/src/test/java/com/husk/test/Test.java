package com.husk.test;

import com.husky.server.test.service.impl.EchoServiceImpl;

/**
 * Created by google on 16/6/3.
 */
public class Test {
    public static void main(String[] args){
        String a = "adb";
        byte[] bytes = a.getBytes();
        for(byte b : bytes){
            System.out.println(b);
        }

    }
}
