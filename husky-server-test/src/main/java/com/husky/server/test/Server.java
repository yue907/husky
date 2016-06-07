package com.husky.server.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by google on 16/5/30.
 */
public class Server {
    public static void main(String[] args){
        ClassPathXmlApplicationContext context =new ClassPathXmlApplicationContext("classpath*:config/spring/appcontext-service.xml");
        context.start();

//        try {
//            Thread.sleep(1000*1000000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        try {
//            System.out.println("任意键退出");
//            System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        context.stop();
//        System.out.println("服务退出");
    }
}
