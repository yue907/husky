package com.husky.server.test.service.impl;

import com.husky.server.test.service.EchoService;

/**
 * Created by google on 16/5/30.
 */
public class EchoServiceImpl implements EchoService{
    public int plus(int x, int y) {
        return x+y;
    }

    public double divide(double x, double y) {
//        if(0 != y){
//            return x/y;
//        }
//        return 0;
  
        return x/y;
    }
}
